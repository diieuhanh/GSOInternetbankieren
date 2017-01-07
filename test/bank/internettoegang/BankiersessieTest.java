/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.internettoegang;

import bank.bankieren.IRekening;
import bank.bankieren.Money;
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

    public BankiersessieTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isGeldig method, of class Bankiersessie.
     */
    @Test
    public void testIsGeldig() {
        System.out.println("isGeldig");
        Bankiersessie instance = null;
        boolean expResult = false;
        boolean result = instance.isGeldig();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of maakOver method, of class Bankiersessie.
     */
    @Test
    public void testMaakOver() throws Exception {
        System.out.println("maakOver");
        int bestemming = 0;
        Money bedrag = null;
        Bankiersessie instance = null;
        boolean expResult = false;
        boolean result = instance.maakOver(bestemming, bedrag);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRekening method, of class Bankiersessie.
     */
    @Test
    public void testGetRekening() throws Exception {
        System.out.println("getRekening");
        Bankiersessie instance = null;
        IRekening expResult = null;
        IRekening result = instance.getRekening();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of logUit method, of class Bankiersessie.
     */
    @Test
    public void testLogUit() throws Exception {
        System.out.println("logUit");
        Bankiersessie instance = null;
        instance.logUit();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
