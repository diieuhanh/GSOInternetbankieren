/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.internettoegang;

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Klant;
import bank.bankieren.Money;
import bank.bankieren.Rekening;
import bank.centrale.ICentraleBank;
import fontys.util.InvalidSessionException;
import static java.lang.Thread.sleep;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hanh-
 */
public class BankiersessieTest {
    private IBank b1, b2;
    private long laatsteAanroep;
    private Klant k1, k2, k3;
    private Rekening r1, r2,r3;
    private ICentraleBank cb1;
    private IBankiersessie bs1, bs2;
    private Balie balie;
    
    
    public BankiersessieTest() {
    }
    
    @Before
    public void setUp() {
        try {
            
            k1 = new Klant("Jansen", "Rotterdam");
            k2 = new Klant("Nikki W.", "Amsterdam");
            k3 = new Klant("W.Helsma", "Eindhoven");
            r1 = new Rekening(100000000, k1, new Money(100, "€"));
            r2 = new Rekening(100000001, k2, new Money(20, "€"));
            r3 = new Rekening(100000002, k3, new Money(2000, "€"));
            
            b1 = new Bank("NSB", cb1);
            b1.openRekening(k1.getNaam(), k1.getPlaats());
            b1.openRekening(k2.getNaam(), k2.getPlaats());
            
            b2 = new Bank("NSB Junior", cb1);
            b2.openRekening(k3.getNaam(), k3.getPlaats());
            
            balie = new Balie(b1);
            
        } catch (RemoteException ex) {
            Logger.getLogger(BankiersessieTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }
    
    
    /**
     * constructor of BankierSessie
     * parameters
     *      int reknr
     *      IBank bank
     */
    @Test
    public  void testBankierSessie() throws RemoteException{
        bs1 = new Bankiersessie(r2.getNr(), b1);
    }
    
    /**
     * Test of isGeldig method, of class Bankiersessie.
     */
    @Test
    public void testIsGeldig() throws RemoteException, InterruptedException {
        System.out.println("isGeldig true");
        Bankiersessie instance = new Bankiersessie(100000000, b1);
        boolean expResult = true;
        boolean result = instance.isGeldig();
        assertEquals(expResult, result);
        
        System.out.println("isGeldig false");
        instance = new Bankiersessie(100000001, b1);
        expResult = false;
        System.out.println(System.currentTimeMillis() + "nu");
        Thread.sleep(7000);
        System.out.println(System.currentTimeMillis() + "later");
        result = instance.isGeldig();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of maakOver method, of class Bankiersessie.
     */
    @Test
    public void testMaakOver() throws Exception {
        System.out.println("maakOver");
        int bestemming = 100000000;
        Money bedrag = new Money(2, "€");
        Bankiersessie instance = new Bankiersessie(100000001, b1);;
        boolean expResult = true;
        boolean result = instance.maakOver(bestemming, bedrag);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getRekening method, of class Bankiersessie.
     */
    @Test
    public void testGetRekening() throws Exception {
        System.out.println("getRekening");
        Bankiersessie instance = new Bankiersessie(100000000, b1);
        IRekening expResult = r1;
        IRekening result = instance.getRekening();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
    @Test (expected = InvalidSessionException.class)
    public void exceptionTestGetRekening() throws Exception {
        System.out.println("getRekening");
        Bankiersessie instance = new Bankiersessie(100000001, b1);
        Thread.sleep(7000);
        IRekening expResult = r2;
        IRekening result = instance.getRekening();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of logUit method, of class Bankiersessie.
     */
    @Test
    public void testLogUit() throws Exception {
        System.out.println("logUit");
        Bankiersessie instance = new Bankiersessie(100000001, b1);
        instance.logUit();
        // TODO review the generated test code and remove the default call to fail.
    }

}
