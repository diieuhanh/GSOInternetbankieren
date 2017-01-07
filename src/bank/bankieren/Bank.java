package bank.bankieren;

import bank.centrale.ICentraleBank;
import fontys.util.*;

import bank.internettoegang.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bank implements IBank {

    /**
     *
     */
    private static final long serialVersionUID = -8728841131739353765L;
    private Map<Integer, IRekeningTbvBank> accounts;
    private Collection<IKlant> clients;
    private int nieuwReknr;
    private String name;
    private ReentrantLock transferLock = new ReentrantLock();
    private ReentrantLock openLock = new ReentrantLock();
    //private IRemotePublisherForListener publisher;
    public RemotePublisher publisher;
    private static boolean createRegistry = true;
    private static int portNumber = 1099;
    private Registry registry = null;

    public Bank(String name, ICentraleBank cb) throws RemoteException {
        try {
            accounts = new HashMap<Integer, IRekeningTbvBank>();
            clients = new ArrayList<IKlant>();
            nieuwReknr = 100000000;
            this.name = name;

            registry = createRegistry();

            publisher = new RemotePublisher();
            publisher.registerProperty("money");

        } catch (RemoteException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int openRekening(String name, String city) {
        if (name.equals("") || city.equals("")) {
            return -1;
        }

        IKlant klant = getKlant(name, city);
        IRekeningTbvBank account = new Rekening(nieuwReknr, klant, Money.EURO);
        accounts.put(nieuwReknr, account);
        nieuwReknr++;
        return nieuwReknr - 1;

    }

    private IKlant getKlant(String name, String city) {
        for (IKlant k : clients) {
            if (k.getNaam().equals(name) && k.getPlaats().equals(city)) {
                return k;
            }
        }
        IKlant klant = new Klant(name, city);
        clients.add(klant);
        return klant;
    }

    public IRekening getRekening(int nr) {
        return accounts.get(nr);
    }

    //methode maakOver() == transfer
    public boolean maakOver(int source, int destination, Money money)
            throws NumberDoesntExistException {
        if (source == destination) {
            throw new RuntimeException(
                    "cannot transfer money to your own account");
        }
        if (!money.isPositive()) {
            throw new RuntimeException("money must be positive");
        }

        IRekeningTbvBank source_account = (IRekeningTbvBank) getRekening(source);
        IRekeningTbvBank destination_account = (IRekeningTbvBank) getRekening(destination);

        if (source_account == null && destination_account == null) {
            throw new NumberDoesntExistException("account " + source
                    + " unknown at " + name);
        }

        Money negative = Money.difference(new Money(0, money.getCurrency()),
                money);
        boolean success = source_account.muteer(negative);
        if (!success) {
            return false;
        }

        IRekeningTbvBank dest_account = (IRekeningTbvBank) getRekening(destination);

        if (dest_account == null) {
            throw new NumberDoesntExistException("account " + destination
                    + " unknown at " + name);
        }
        success = dest_account.muteer(money);

        if (!success) // rollback
        {
            source_account.muteer(money);
        }

        if (success) {
            try {
                publisher.inform("money", null, money);
            } catch (RemoteException ex) {
                Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return success;
    }

    @Override
    public String getName() {
        return name;
    }

    private Registry createRegistry() {

        // Create registry at port number
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(portNumber);
            System.out.println("Server: Registry created on port number " + portNumber);
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot create registry");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            registry = null;
        }
        return registry;
    }

}
