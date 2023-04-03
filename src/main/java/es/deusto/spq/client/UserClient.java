package es.deusto.spq.client;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.deusto.spq.pojo.UserData;

public class UserClient extends JFrame {

    public UserClient(UserData userData) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    
        JPanel loginPanel = new JPanel();
        JLabel loginKeyLabel = new JLabel("Login");
        JLabel loginValueLabel = new JLabel(userData.getLogin());
        loginPanel.add(loginKeyLabel);
        loginPanel.add(loginValueLabel);
    
        JPanel passwordPanel = new JPanel();
        JLabel passwordKeyLabel = new JLabel("Password");
        JLabel passwordValueLabel = new JLabel(userData.getPassword());
        passwordPanel.add(passwordKeyLabel);
        passwordPanel.add(passwordValueLabel);

        JPanel namePanel = new JPanel();
        JLabel nameKeyLabel = new JLabel("Name");
        JLabel nameValueLabel = new JLabel(userData.getName());
        namePanel.add(nameKeyLabel);
        namePanel.add(nameValueLabel);

        JPanel surnamePanel = new JPanel();
        JLabel surnameKeyLabel = new JLabel("Surname");
        JLabel surnameValueLabel = new JLabel(userData.getSurname());
        surnamePanel.add(surnameKeyLabel);
        surnamePanel.add(surnameValueLabel);

        add(loginPanel);
        add(passwordPanel);
        add(namePanel);
        add(surnamePanel);
    }
}
