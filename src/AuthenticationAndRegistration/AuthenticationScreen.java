package src.AuthenticationAndRegistration;

import src.Database;
import src.MainScreen;
import src.Screen;
import src.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Statement;

public class AuthenticationScreen extends Screen {
    private JTextArea usernameField;
    private JButton signUpButton;
    private JPasswordField passwordField;
    private JLabel usernameLabel;
    private JLabel PasswordLabel;
    private JButton returnButton;
    private JButton exitButton;
    private JPanel panel;
    private JButton signInButton;

    public AuthenticationScreen(User user, Screen prev_screen, Screen next_screen){
        super(user,prev_screen,next_screen);
    }
    @Override
    public void CreateScreen(){
        super.CreateScreen();
        frame.setContentPane(panel);
        signInButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                user = new User();
                user.username=usernameField.getText();
                user.password=new String(passwordField.getPassword());
                Statement st = Database.connectToDatabase("bank_system", "root", "password");
                if(Database.verifyUser(st,user.username,user.password)){
                    JOptionPane.showMessageDialog(frame, "user successfully verified");
                    frame.dispose();
                    user.ordinary_account_number = Database.getOrdinaryAccountNumber(st, user.username);
                    user.savings_account_number = Database.getSavingsAccountNumber(st, user.username);
                    if(next_screen!=null){
                        if(user.ordinary_account_number==null || user.savings_account_number==null){
                            new MainScreen(user, prev_screen, new Screen()).CreateScreen();
                        }
                        else{
                            new ChooseAccountNumberScreen(user, AuthenticationScreen.this,new Screen()).CreateScreen();
                        }
                    }
                }
                else{
                    JOptionPane.showMessageDialog(frame, "user doesn't exist");
                }
            }
        });
        signUpButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                frame.dispose();
                new RegistrationScreen1(user, AuthenticationScreen.this, new Screen()).CreateScreen();
            }
        });
        returnButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                frame.dispose();
                if(prev_screen!=null){
                    prev_screen.CreateScreen();
                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.dispose();
            }
        });
        frame.setSize(800,600);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new AuthenticationScreen(null,null,new Screen()).CreateScreen();
    }
}
