/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.bankieren;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author hanh-
 */
public interface IBeveiligd extends Remote {

    boolean muteer(int rekeningnr, Money money) throws RemoteException;

}
