/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.gui;

import bank.bankieren.IRekening;
import bank.bankieren.Money;
import bank.internettoegang.IBalie;
import bank.internettoegang.IBankiersessie;
import bank.internettoegang.IRemotePropertyListener;
import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import bank.internettoegang.*;
import javafx.application.Platform;

/**
 * FXML Controller class
 *
 * @author frankcoenen
 */
public class BankierSessieController implements Initializable, IRemotePropertyListener {

    @FXML
    private Hyperlink hlLogout;

    @FXML
    private TextField tfNameCity;
    @FXML
    private TextField tfAccountNr;
    @FXML
    private TextField tfBalance;
    @FXML
    private TextField tfAmount;
    @FXML
    private TextField tfToAccountNr;
    @FXML
    private Button btTransfer;
    @FXML

    private TextArea taMessage;

    private BankierClient application;
    private IBalie balie;
    private IBankiersessie sessie;
    private Money money;
    private Registry registry = null;
    private IRemotePublisherForListener publisher;
    private String prop = "b";

    public void setApp(BankierClient application, IBalie balie, IBankiersessie sessie) {
        this.balie = balie;
        this.sessie = sessie;
        this.application = application;
        IRekening rekening = null;
        
        sessie.addListener(this, prop);
        
        try {
            rekening = sessie.getRekening();
            tfAccountNr.setText(rekening.getNr() + "");
            tfBalance.setText(rekening.getSaldo() + "");
            String eigenaar = rekening.getEigenaar().getNaam() + " te "
                    + rekening.getEigenaar().getPlaats();
            tfNameCity.setText(eigenaar);
        } catch (InvalidSessionException ex) {
            taMessage.setText("bankiersessie is verlopen");
            Logger.getLogger(BankierSessieController.class.getName()).log(Level.SEVERE, null, ex);

        } catch (RemoteException ex) {
            taMessage.setText("verbinding verbroken");
            Logger.getLogger(BankierSessieController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        registry = locateRegistry("localhost", 1099);

        try {
            publisher = (IRemotePublisherForListener) registry.lookup("remotePublisher");
            publisher.subscribeRemoteListener(this, "money");
            
            if (publisher != null) {
                System.out.println("Client: Remote publisher bound");
            } else {
                System.out.println("Client: Could not bind remote publisher");
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(BankierSessieController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            sessie.logUit();
            application.gotoLogin(balie, "");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void transfer(ActionEvent event) {
        try {
            int from = Integer.parseInt(tfAccountNr.getText());
            int to = Integer.parseInt(tfToAccountNr.getText());
            if (from == to) {
                taMessage.setText("can't transfer money to your own account");
            }
            long centen = (long) (Double.parseDouble(tfAmount.getText()) * 100);
            sessie.maakOver(to, new Money(centen, Money.EURO));
        } catch (RemoteException e1) {
            e1.printStackTrace();
            taMessage.setText("verbinding verbroken");
        } catch (NumberDoesntExistException | InvalidSessionException e1) {
            e1.printStackTrace();
            taMessage.setText(e1.getMessage());
        }
    }

    /*@Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        money = (Money) evt.getNewValue();
        tfBalance.setText(money.toString());

    }*/
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        BankierSessieController banksessieControl = this;
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                if(evt.getNewValue() == null){
                    try {
                        taMessage.setText("Sessie verlopen");
                        //remove listener als bankiersessie is afgelopen
                        sessie.removeListener(this, prop);
                        sessie.logUit();
                    } catch (RemoteException ex) {
                        Logger.getLogger(BankierSessieController.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("error propertyChange: " + ex.getMessage());
                    }
                } else{
                    try {
                        if(sessie.isGeldig()){
                            money = (Money) evt.getNewValue();
                            tfBalance.setText(money.toString());
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(BankierSessieController.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("error propertyChange: " + ex.getMessage());
                    }
                }
                
            }
            
        });

    }

    private Registry locateRegistry(String ipAddress, int portNumber) {

        // Locate registry at IP address and port number
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: RemoteException: " + ex.getMessage());
            registry = null;
        }
        return registry;
    }
}
