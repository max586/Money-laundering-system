package src.Credits;
import java.util.Date;

import src.Database;
import src.Screen;
import src.User;
import java.lang.Object;
import java.util.Date;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JPanel;
import java.sql.Statement;

public class Credit extends Screen
{
    public JPanel CreditPanel;
    public JLabel CreditLogo;
    public JLabel AccNum;
    public JTextField Amount;
    public JTextField Years;
    public JCheckBox yesCheckBox;
    public JLabel Balance;
    public JButton takeNewCreditButton;
    public JLabel MyCreditAmount;
    public JLabel MyPayedCredit;
    public JLabel MyDebt;
    public JButton payDebtButton;
    public JButton prevButton;
    public JOptionPane jpane;
    public JDialog jdialog;


    public Credit(User user, Screen prev_screen, Screen next_screen){
        super(user,prev_screen,next_screen);
        jpane = new JOptionPane();
        jdialog=jpane.createDialog(CreditPanel,"");

        takeNewCreditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(!Database.hasCredit(user.username))//jezeli uzitkownik nie ma kredytu
                {
                        if(yesCheckBox.isSelected())
                        {
                            jpane.setMessage("Now you must pay us some money, hehe");
                            Database.addCredit(user.username,Float.parseFloat(Amount.getText()),0,new Date().toString(),Integer.parseInt(Years.getText()));
                            String[] CreditInfo = Database.getCredit(user.username);//Amount , AmountPayed , StartDate , Duration
                            MyCreditAmount.setText(CreditInfo[0]);
                            MyPayedCredit.setText(CreditInfo[1]);
                            MyDebt.setText(String.valueOf(checkDebt()));
                        }else
                        {
                            jpane.setMessage("you must accept the terms to take the credit ");
                        }

                }else
                {
                    jpane.setMessage("You already have a credit");
                }
                jpane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
                jdialog.setTitle("Info");
                jdialog.setSize(450,170);
                jdialog.setVisible(true);
            }
        });
        payDebtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Database.hasCredit(user.username)) {
                    String[] CreditInfo = Database.getCredit(user.username);//Amount , AmountPayed , StartDate , Duration
                    float UserBalance = Database.getOrdinaryAccountBalance(user.username);
                    float CreditDebt = checkDebt();
                    float CreditPayed = Float.parseFloat(CreditInfo[1]);

                    if (UserBalance - CreditDebt >= 0 && Database.hasCredit(user.username)) {
                        Database.setOrdinaryAccountBalance(user.username, UserBalance - CreditDebt);
                        Database.setCreditAmountPayed(user.username, CreditPayed + CreditDebt);
                        CreditInfo = Database.getCredit(user.username);
                        Balance.setText(String.valueOf(Database.getOrdinaryAccountBalance(user.username)));
                        MyCreditAmount.setText(CreditInfo[0]);
                        MyPayedCredit.setText(CreditInfo[1]);
                        MyDebt.setText(String.valueOf(checkDebt()));

                        Date StartDate = new Date(Integer.parseInt(CreditInfo[2].substring(0, 3)) - 1900, Integer.parseInt(CreditInfo[2].substring(5, 6)), Integer.parseInt(CreditInfo[2].substring(8, 9)));
                        Date Today = new Date();
                        Date DiffOfYears = new Date(Today.getTime() - StartDate.getTime());
                        int currentYear = DiffOfYears.getYear() - 70;
                        if (currentYear == Integer.parseInt(CreditInfo[3]) && checkDebt() == 0) ;
                        {
                            Database.deleteCredit(user.username);
                        }
                    }
                }else
                {
                    jpane.setMessage("You don't have credit");
                    jpane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
                    jdialog.setTitle("Info");
                    jdialog.setSize(450,170);
                    jdialog.setVisible(true);
                }
            }
        });
    }
    public void CreateScreen() {

        frame = new JFrame();

        frame.setTitle("Credit");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(CreditPanel);

        AccNum.setText(Database.getOrdinaryAccountNumber(user.username));
        Balance.setText(String.valueOf(Database.getOrdinaryAccountBalance(user.username)));
        //System.out.println(Balance.getText());
        String[] CreditInfo = Database.getCredit(user.username);//Amount , AmountPayed , StartDate , Duration

        if(Database.hasCredit(user.username))
        {
        MyCreditAmount.setText(CreditInfo[0]);
        MyPayedCredit.setText(CreditInfo[1]);
        MyDebt.setText(String.valueOf(checkDebt()));
        }
        else
        {
            MyCreditAmount.setText("0");
            MyPayedCredit.setText("0");
            MyDebt.setText("0");
        }

        prevButton.addActionListener(e->
        {
            frame.dispose();
            if(prev_screen!=null){
                prev_screen.frame.setVisible(true);
            }

        });
        frame.setSize(1080, 720);
        frame.setVisible(true);
    }
    public float checkDebt()
    {
        String[] CreditInfo = Database.getCredit(user.username);//Amount , AmountPayed , StartDate , Duration
        float creditAmount = Float.parseFloat(CreditInfo[0]);
        float currentCreditPayment = Float.parseFloat(CreditInfo[1]);
        float yearsAll = Float.parseFloat(CreditInfo[3]);
        //Date StartDate = new Date(Integer.parseInt(CreditInfo[2].substring(0,3))-1900,Integer.parseInt(CreditInfo[2].substring(5,6)),Integer.parseInt(CreditInfo[2].substring(8,9)));
        Date StartDate = new Date(2000-1900,4,8);
        Date Today = new Date();
        Date DiffOfYears = new Date(Today.getTime() - StartDate.getTime());
        int currentYear = DiffOfYears.getYear()-70;



        //System.out.println(currentYear);
        float needToPay = 0;
        float percent = (float)0.05;
        for (int i = 0; i < currentYear && i < yearsAll; i++)
        {
            needToPay+=creditAmount/yearsAll + (creditAmount - i*creditAmount/yearsAll)*percent;
        }
            System.out.println(needToPay);
            return (needToPay - currentCreditPayment);


    }

    public static void main(String[] args) throws IOException {
        User test_user = new User();
        test_user.username="test_user";
        new Credit(test_user,null,null).CreateScreen();
    }

}
