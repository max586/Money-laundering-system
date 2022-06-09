package src;
import src.AuthenticationAndRegistration.AuthenticationScreen;
import src.Credits.Credit;
import src.Settings.SettingsMainScreen;
import src.mainFrame.MainFrame;
import src.timer.AppTimer;
import src.timer.MouseAction;
import src.transfers.AccountChoosed;
import src.transfers.TransferFactory;

import javax.swing.*;
import java.io.IOException;
import javax.swing.JPanel;
import java.awt.event.*;

public class MainScreen extends Screen {
    public JPanel AuthPanel;
    public JButton PROFILButton;
    public JButton incountryButton;
    public JButton KREDYTYButton;
    public JButton wylogujButton;
    public JButton prevButton;
    public JLabel timeLabel;
    public JLabel AccNumber;
    public JLabel AccType;
    public JButton foreignTransferButton;
    public JButton ownTransferButton;
    public JButton standingOrderTransferButton;
    private JButton historyButton;
    public int counter = 0;
    String chosenAcc;
    String []options = {"one","two"};


    public MainScreen(User user, Screen prev_screen, Screen next_screen, String option){
        super(user,prev_screen,next_screen);
        chosenAcc = option;
    }

    public void CreateScreen(){

        frame = new JFrame();
        frame.setSize(1080,720);
        frame.setTitle("MainScreen");
        AppTimer appTimer = new AppTimer(timeLabel,this);
        AuthPanel.addMouseMotionListener(new MouseAction(appTimer));
        appTimer.start();

        if(chosenAcc == "ordinary")
        {
            String temp = Database.getOrdinaryAccountNumber(user.username);
            AccNumber.setText(temp);
            AccType.setText("Wybrane konto: ordinary");
        }
        else if(chosenAcc == "saving")
        {
            AccNumber.setText(Database.getSavingsAccountNumber( user.username));
            AccType.setText("Wybrane konto: saving");
        }

//Incountry Transfer, Foreign Transfer, Own Transfer, Standing Order Transfer, BLIK Phone Transfer
        foreignTransferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chosenAcc == "ordinary") {
                    frame.dispose();
                    try {
                        new TransferFactory(AccountChoosed.ORDINARYACCOUNT, user, new MainFrame()).getTransfer(TransferFactory.TransferType.ZAGRANICZNY);
                    } catch (Exception e2) {
                    }
                }
            }
        });

        incountryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chosenAcc == "ordinary") {
                    frame.dispose();
                    try {
                        new TransferFactory(AccountChoosed.ORDINARYACCOUNT, user, new MainFrame()).getTransfer(TransferFactory.TransferType.KRAJOWY);
                    } catch (Exception e2) {
                    }
                }
            }
        });

        ownTransferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
;                try {
                    new TransferFactory(AccountChoosed.ORDINARYACCOUNT, user, new MainFrame()).getTransfer(TransferFactory.TransferType.WLASNY);
                }catch(Exception e2){}
            }
        });

        standingOrderTransferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chosenAcc == "ordinary") {
                    frame.dispose();
                    try {
                        new TransferFactory(AccountChoosed.ORDINARYACCOUNT, user, new MainFrame()).getTransfer(TransferFactory.TransferType.ZLECENIESTALE);
                    } catch (Exception e2) {
                    }
                }
            }
        });
        KREDYTYButton.addActionListener(e->
        {
            frame.dispose();
            new Credit(user, MainScreen.this, new Screen()).CreateScreen();
        });
        PROFILButton.addActionListener(e->
        {
            frame.dispose();
            new SettingsMainScreen(user, MainScreen.this, new Screen()).CreateScreen();
        });

        wylogujButton.addActionListener(e->
        {
            frame.dispose();
            new AuthenticationScreen(null,null,new Screen()).CreateScreen();
        });

        prevButton.addActionListener(e->
        {
            frame.dispose();
            if(prev_screen!=null){
                prev_screen.frame.setVisible(true);
        }

        });
        frame.setContentPane(AuthPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        User test_user = new User();
        test_user.username="test_user";
        new MainScreen(test_user,null,null, "ordinary").CreateScreen();
    }
}

