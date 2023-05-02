package es.deusto.spq.client;

import javax.swing.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.awt.*;
import java.awt.event.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.pojo.UserData;

class LoginClient extends JFrame implements ActionListener {
    JButton b1;
    JPanel panel, panel1, panel2, panel3;
    JLabel userLabel, passLabel;
    final JTextField textField1, textField2;
    private String hostname, port;

    public LoginClient(String hostname, String port) {
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(500,300);

        this.hostname = hostname;
        this.port = port;
        client = ClientBuilder.newClient();
        webTarget = client.target(String.format("http://%s:%s/rest/resource", hostname, port));

        userLabel = new JLabel();
        userLabel.setText("Login");

        textField1 = new JTextField(15);

        passLabel = new JLabel();
        passLabel.setText("Password");

        textField2 = new JPasswordField(15);

        panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);

        panel1 = new JPanel();
        panel1.add(userLabel);
        panel1.add(textField1);

        panel2 = new JPanel();
        panel1.add(passLabel);
        panel1.add(textField2);
        
        panel3 = new JPanel();
        b1 = new JButton("SUBMIT");
        panel3.add(b1);

        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);

        add(panel, BorderLayout.CENTER);

        b1.addActionListener(this);
        setTitle("LOGIN FORM");
    }

    protected static final Logger logger = LogManager.getLogger();
    private Client client;
    private WebTarget webTarget;

    public void actionPerformed(ActionEvent ae) {
        String userValue = textField1.getText();
        String passValue = textField2.getText();

        WebTarget loginUserWebTarget = webTarget.path("login").queryParam("login", userValue).queryParam("password",
                passValue);
        Invocation.Builder invocationBuilder = loginUserWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Error connecting with the server. Code: {}", response.getStatus());
            JOptionPane.showMessageDialog(this, "Error " + response.getStatus(), "Failure", JOptionPane.ERROR_MESSAGE);
        } else {
            logger.info("User correctly logged in");
            UserData user = response.readEntity(UserData.class);
            switch (user.getRole()) {
                case STUDENT:
                    StudentClient userClient = new StudentClient(user);
                    setVisible(false);
                    userClient.setVisible(true);
                    break;
                
                case ADMIN:
                    AdminClient adminClient = new AdminClient(user, hostname, port);
                    setVisible(false);
                    adminClient.setVisible(true);
                    break;

                case PROFESSOR:
                    ProfessorClient professorClient = new ProfessorClient(user, hostname, port);
                    setVisible(false);
                    professorClient.setVisible(true);
                    break;
            
                default:
                    logger.error("Unrecognized role");
                    break;
            }
            
        }
    }
}
