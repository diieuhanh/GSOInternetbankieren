package bank.internettoegang;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import bank.gui.BankierSessieController;

import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bankiersessie extends UnicastRemoteObject implements
        IBankiersessie{

    private static final long serialVersionUID = 1L;
    private long laatsteAanroep;
    private int reknr;
    private IBank bank;
    private IRemotePropertyListener listener;

    private IRemotePublisherForListener publisher;

    public Bankiersessie(int reknr, IBank bank) throws RemoteException {
        laatsteAanroep = System.currentTimeMillis();
        this.reknr = reknr;
        this.bank = bank;

    }

    public boolean isGeldig() {
        return System.currentTimeMillis() - laatsteAanroep < GELDIGHEIDSDUUR;
        
    }

    @Override
    public boolean maakOver(int bestemming, Money bedrag)
            throws NumberDoesntExistException, InvalidSessionException,
            RemoteException {

        updateLaatsteAanroep();

        if (reknr == bestemming) {
            throw new RuntimeException(
                    "source and destination must be different");
        }
        if (!bedrag.isPositive()) {
            throw new RuntimeException("amount must be positive");
        }

        return bank.maakOver(reknr, bestemming, bedrag);
    }

    private void updateLaatsteAanroep() throws InvalidSessionException {
        if (!isGeldig()) {
            throw new InvalidSessionException("session has been expired");
        }

        laatsteAanroep = System.currentTimeMillis();
    }

    @Override
    public IRekening getRekening() throws InvalidSessionException,
            RemoteException {

        updateLaatsteAanroep();

        return bank.getRekening(reknr);
    }

    @Override
    public void logUit() throws RemoteException {
        UnicastRemoteObject.unexportObject(this, true);
    }
    
    @Override
    public void addListener(BankierSessieController aThis, String prop) {
        try {
            publisher.subscribeRemoteListener(listener, prop);
        } catch (RemoteException ex) {
            Logger.getLogger(Bankiersessie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void removeListener(Runnable aThis, String prop) {
        try {
            publisher.unsubscribeRemoteListener(listener, prop);
        } catch (RemoteException ex) {
            Logger.getLogger(Bankiersessie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
