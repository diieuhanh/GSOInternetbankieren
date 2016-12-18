/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.bankieren;

import fontys.util.NumberDoesntExistException;
import java.rmi.RemoteException;
import java.util.*;
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
 * @author P u c k
 */
public class BankIT {
    private Klant k1, k2;
    private Money m1, m2;
    private Rekening r1, r2;
    private Bank b1, b2;
    
    @Before
    public void setUp() {
        k1 = new Klant("Jansen", "Rotterdam");
        k2 = new Klant("Nikki W.", "Ämsterdam");
        m1 = new Money(100, "€");
        m2 = new Money(20, "€");
        r1 = new Rekening(100000000, k1, m1);
        r2 = new Rekening(100000001, k2, m2);
        b1 = new Bank("NSB");
        b1.openRekening(k1.getNaam(), k1.getPlaats());
        b1.openRekening(k2.getNaam(), k2.getPlaats());
    }
    
    @After
    public void tearDown() {
        
    }

    /**
     *  Constructor of Bank
     *   Bank(string name)
     */
    @org.junit.Test
    public void testConstructor(){
        System.out.println("constructor");
        String exp = "NSB Rotterdam";
        b2 = new Bank(exp);
        assertEquals(exp, b2.getName());
        
    }
    
    /**
     * Test of openRekening method, of class Bank.
     */
    @org.junit.Test
    public void testOpenRekening() {
        System.out.println("openRekening");
        // TODO review the generated test code and remove the default call to fail.
        String name = "";
        String city = "";
        int exp = -1;
        int result = b1.openRekening(name, city);
        assertEquals(exp, result);
        
        name = k1.getNaam();
        city = k1.getPlaats();
        exp = 1;
        result = b1.openRekening(name, city);
    }

    /**
     * Test of getRekening method, of class Bank.
     */
    @org.junit.Test
    public void testGetRekening() {
        System.out.println("getRekening");
        int nr = r1.getNr();
        Bank instance = b1;
        b1.openRekening(k1.getNaam(), k1.getPlaats());
        b1.openRekening(k2.getNaam(), k2.getPlaats());
        IRekening exp = r1;
        IRekening result = instance.getRekening(nr);
        // TODO review the generated test code and remove the default call to fail.
        assertEquals(exp, result);
        
        exp = r2;
        result = instance.getRekening(r2.getNr());
        assertEquals(exp, result);
    }

    /**
     * Test of maakOver method, of class Bank.
     */
    @org.junit.Test
    public void testMaakOver() {
        try {
            int source = r1.getNr();
            int destination = r2.getNr();
            Money m1= new Money(5, "€");
            Bank instance = b1;
            instance.openRekening(k1.getNaam(), k1.getPlaats());
            instance.openRekening(k2.getNaam(), k2.getPlaats());
            // TODO review the generated test code and remove the default call to fail.
            boolean exp = true;
            boolean result = instance.maakOver(source, destination, m1);
            assertEquals(exp, result);

        } catch (NumberDoesntExistException ex) {
            Logger.getLogger(BankIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @org.junit.Test (expected = RuntimeException.class)
    public void testMaakOverRunTimeException(){
        try {
            Bank instance = b1;
            //Money negative = new Money(-10, "€");
            Money positive = new Money(100, "€");
            instance.maakOver(100000000, 100000000, positive);
        } catch (NumberDoesntExistException ex) {
            Logger.getLogger(BankIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @org.junit.Test (expected = RuntimeException.class)
    public void testMaakOverRunTimeException2() {
        try {
            Bank instance = b1;
            Money negative = new Money(-10, "€");
            System.out.println("Money positive is : "+negative.isPositive());
            
            instance.maakOver(100000000, 100000001, negative);
        } catch (NumberDoesntExistException ex) {
            Logger.getLogger(BankIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    @org.junit.Test 
    public void testMaakOverNumberDoesntExistException(){
        Bank instance = b1;
        Money money = new Money(100, "€");
        instance.openRekening(k1.getNaam(), k1.getPlaats());
        instance.openRekening(k2.getNaam(), k2.getPlaats());
        try{
            //number source 10000003 doesnt exist
            instance.maakOver(100000003, 100000001, m1);
        }catch(NumberDoesntExistException ex1){
            
        }
    }
    
    
            
            
    /**
     * Test of getName method, of class Bank.
     */
    @org.junit.Test
    public void testGetName() {
        Bank instance = new Bank("RaDoKo");
        String exp= "RaDoKo";
        String result = instance.getName();
        assertEquals(exp, result);
        // TODO review the generated test code and remove the default call to fail.
    }
    
}
