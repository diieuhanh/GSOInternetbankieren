/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.centrale;

import java.rmi.Remote;
import bank.bankieren.*;
import java.rmi.RemoteException;
import fontys.util.NumberDoesntExistException;

/**
 *
 * @author hanh-
 */
public interface ICentraleBank extends Remote {

    void addBank(String bankName, IBeveiligd bank);

    IBeveiligd getRekening(int rekeningNr) throws NumberDoesntExistException;

    boolean maakOver(int source, int destination, Money amount) throws RemoteException, NumberDoesntExistException;

}
