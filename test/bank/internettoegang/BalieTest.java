/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.internettoegang;

import bank.bankieren.Bank;
import bank.centrale.CentraleBank;
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
public class BalieTest {

    private Bank bank;
    private Balie balie;
    private CentraleBank centraleBank;

    public BalieTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        centraleBank = new CentraleBank();
        bank = new Bank("Rabo", centraleBank);
        balie = new Balie(bank);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of openRekening method, of class Balie.
     */
    @Test
    public void testOpenRekening() {
        String naam = "SNS";
        String plaats = "Eindhoven";
        String wachtwoord = "1234";

        assertEquals("naam is leeg", null, balie.openRekening(" ", plaats, wachtwoord));
        assertEquals("plaats is leeg", null, balie.openRekening(naam, " ", wachtwoord));
        assertEquals("wachtwoord is leeg", null, balie.openRekening(naam, plaats, " "));

        assertEquals("wachtwoord is korter dan 4 tekens.", null, balie.openRekening(naam, plaats, "123"));
        assertEquals("wachtwoord is langer dan 8 tekens.", null, balie.openRekening(naam, plaats, "SNSEindhoven"));

    }

    /**
     * Test of logIn method, of class Balie.
     */
    @Test
    public void testLogIn() throws Exception {
        System.out.println("logIn");
        String accountnaam = "HanhTran";
        String wachtwoord = "Hanh1234";
        String login = balie.openRekening(accountnaam, wachtwoord, wachtwoord);

        assertEquals("String accountname is leeg", null, balie.logIn("", wachtwoord));

        assertEquals("String wachtwoord is leeg", null, balie.logIn(login, ""));

    }

}
