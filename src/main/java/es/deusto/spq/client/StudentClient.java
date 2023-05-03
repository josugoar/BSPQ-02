package es.deusto.spq.client;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.pojo.ScoreData;
import es.deusto.spq.pojo.UserData;

public class StudentClient extends JFrame {

	protected static final Logger logger = LogManager.getLogger();

    private Client client;
	private WebTarget webTarget;
	private UserData user;

    public StudentClient(UserData userData, String hostname, String port) {
        this.user = userData;
		client = ClientBuilder.newClient();
		webTarget = client.target(String.format("http://%s:%s/rest/resource", hostname, port));

        JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);

        Vector<Vector<String>> data = new Vector<>();
        for (ScoreData score : getScores()) {
            Vector<String> dataScore = new Vector<>();
            dataScore.add(score.getSubject().getName());
            dataScore.add(score.getSubject().getProffessor().getName());
            dataScore.add(score.getScore().toString());

            data.add(dataScore);
        }

        Vector<String> column = new Vector<>();
        column.add("Subject");
        column.add("Proffessor");
        column.add("Score");
		
		JTable table = new JTable(data, column);
        table.setEnabled(false);
		table.setColumnSelectionAllowed(true);
		scrollPane.setViewportView(table);
		
		JPanel panel = new JPanel();
		scrollPane.setColumnHeaderView(panel);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(600,500);

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

        panel.add(loginPanel);
        panel.add(passwordPanel);
        panel.add(namePanel);
        panel.add(surnamePanel);

        add(panel);
    }

    private List<ScoreData> getScores(){
		WebTarget registerUserWebTarget = webTarget.path("scores")
			.queryParam("login", user.getLogin()).queryParam("password", user.getPassword());
		Invocation.Builder invocationBuilder = registerUserWebTarget.request(MediaType.APPLICATION_JSON);

		Response response = invocationBuilder.get();
		if (response.getStatus() != Status.OK.getStatusCode()) {
			logger.error("Error connecting with the server. Code: {}", response.getStatus());
		} else {
			logger.info("User correctly updated");
		}
		return Arrays.asList(response.readEntity(ScoreData[].class));
	}

}
