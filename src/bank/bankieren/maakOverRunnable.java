/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.bankieren;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author P u c k
 */
public class maakOverRunnable implements Runnable{
    private int tegenRekening;
    private double bedrag;
    private Bank bank;
    
    public maakOverRunnable(int tegenRekening, double bedrag, Bank bank){
        this.tegenRekening = tegenRekening;
        this.bedrag = bedrag;
        this.bank = bank;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("maakOverRunnable()");
            
            Thread.sleep(80);
        } catch (InterruptedException ex) {
            Logger.getLogger(maakOverRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
