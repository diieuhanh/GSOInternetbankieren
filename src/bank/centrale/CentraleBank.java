/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.centrale;

import bank.bankieren.IBank;
import bank.bankieren.IBeveiligd;
import bank.bankieren.Money;
import fontys.util.NumberDoesntExistException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author hanh-
 */
public class CentraleBank extends UnicastRemoteObject implements ICentraleBank {

    private Map<Integer, String> rekeningen;
    private Map<String, IBeveiligd> banken;

    public CentraleBank() throws RemoteException {
        banken = new HashMap<>();
        rekeningen = new HashMap<>();
    }

    @Override
    public void addBank(String bankName, IBeveiligd bank) {
        banken.put(bankName, bank);
    }

    @Override
    public IBeveiligd getRekening(int rekeningNr) throws NumberDoesntExistException {
        String name = rekeningen.get(rekeningNr);
        return banken.get(name);
    }

    @Override
    public boolean maakOver(int source, int destination, Money amount) throws RemoteException, NumberDoesntExistException {
        IBeveiligd src = getRekening(source);
        IBeveiligd dst = getRekening(destination);

        Money negative = Money.difference(new Money(0, amount.getCurrency()),
                amount);

        boolean success = src.muteer(source, negative);
        if (!success) {
            return false;
        }

        success = dst.muteer(destination, amount);

        if (!success) // rollback
        {
            src.muteer(source, amount);
        }
        return success;
    }

}
