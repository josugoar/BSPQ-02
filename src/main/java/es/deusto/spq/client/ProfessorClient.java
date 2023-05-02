package es.deusto.spq.client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.FlowLayout;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import es.deusto.spq.pojo.ScoreData;
import es.deusto.spq.pojo.UserData;
import es.deusto.spq.pojo.SubjectData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ProfessorClient extends JFrame {
	private JPanel contentPane;
	private JTextField textID;
	private JTextField textScore;
	private JTable table;

    private Client client;
	private WebTarget webTarget;
    protected static final Logger logger = LogManager.getLogger();
	private UserData user;

    public ProfessorClient(UserData user, String hostname, String port) {
		this.user = user;
        client = ClientBuilder.newClient();
		webTarget = client.target(String.format("http://%s:%s/rest/resource", hostname, port));

    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel panelLogout = new JPanel();
		contentPane.add(panelLogout);
		panelLogout.setLayout(new BoxLayout(panelLogout, BoxLayout.X_AXIS));
		
		JPanel panelWindowTitle = new JPanel();
		FlowLayout fl_panelWindowTitle = (FlowLayout) panelWindowTitle.getLayout();
		fl_panelWindowTitle.setAlignment(FlowLayout.LEFT);
		panelLogout.add(panelWindowTitle);
		
		JLabel lblDashboard = new JLabel("Insertar Calificaciones");
		panelWindowTitle.add(lblDashboard);
		lblDashboard.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JPanel panelLogoutBtn = new JPanel();
		FlowLayout fl_panelLogoutBtn = (FlowLayout) panelLogoutBtn.getLayout();
		fl_panelLogoutBtn.setAlignment(FlowLayout.RIGHT);
		panelLogout.add(panelLogoutBtn);
		
		JButton btnLogout = new JButton("Cerrar Sesion");
		panelLogoutBtn.add(btnLogout);
		btnLogout.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnLogout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);			
			}
		});
		
		JPanel panelContent = new JPanel();
		contentPane.add(panelContent);
		panelContent.setLayout(new BoxLayout(panelContent, BoxLayout.X_AXIS));
		
		JSplitPane splitPane = new JSplitPane();
		panelContent.add(splitPane);
		
		JPanel panelLeft = new JPanel();
		splitPane.setLeftComponent(panelLeft);
		panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
		
		JPanel panelEmail = new JPanel();
		panelLeft.add(panelEmail);
		
		JLabel lblEmail = new JLabel("ID");
		panelEmail.add(lblEmail);
		
		textID = new JTextField();
		panelEmail.add(textID);
		textID.setColumns(30);
		
		JPanel panelScore = new JPanel();
		panelLeft.add(panelScore);
		
		JLabel lblScore = new JLabel("Nota");
		panelScore.add(lblScore);
		
		textScore = new JTextField();
		textScore.setColumns(30);
		panelScore.add(textScore);
		
		JPanel panelName = new JPanel();
		panelLeft.add(panelName);
		
		JLabel lblName = new JLabel("Asignatura");
		panelName.add(lblName);
		
		JComboBox<SubjectData> subjectComboBox = new JComboBox<>();
		panelName.add(subjectComboBox);
		
		
		JPanel panelCreateEditAccountBtns = new JPanel();
		panelCreateEditAccountBtns.setBorder(UIManager.getBorder("DesktopIcon.border"));
		panelLeft.add(panelCreateEditAccountBtns);
		panelCreateEditAccountBtns.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnSaveScore = new JButton("Guardar");
		btnSaveScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnSaveScore.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelCreateEditAccountBtns.add(btnSaveScore);
		
		JButton btnCancel = new JButton("Cancelar");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelCreateEditAccountBtns.add(btnCancel);
		
		
		
		JSplitPane splitPaneRight = new JSplitPane();
		splitPaneRight.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPaneRight);
		
		JPanel panelTable = new JPanel();
		splitPaneRight.setLeftComponent(panelTable);
		panelTable.setLayout(new BoxLayout(panelTable, BoxLayout.Y_AXIS));
		
		JPanel panelTableTitle = new JPanel();
		panelTableTitle.setBorder(UIManager.getBorder("DesktopIcon.border"));
		panelTable.add(panelTableTitle);
		
		JLabel lblTableTitle = new JLabel("Calificaciones generales de la asignatura");
		lblTableTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelTableTitle.add(lblTableTitle);
		
		JPanel panelSeparatorRight = new JPanel();
		panelTable.add(panelSeparatorRight);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Email", "Nombre", "Calificacion", "Asignatura"
			}
		) {
			Class<?>[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class
			};
			public Class<?> getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});

		JScrollPane scrollTable = new JScrollPane(table);
		panelTable.add(scrollTable);
		
		JPanel panelAccountInfo = new JPanel();
		splitPaneRight.setRightComponent(panelAccountInfo);
		panelAccountInfo.setLayout(new BoxLayout(panelAccountInfo, BoxLayout.Y_AXIS));
		
		JPanel panelChangeSubject = new JPanel();
		panelAccountInfo.add(panelChangeSubject);
		
		JButton btnChangeSubject = new JButton("Cambiar de Asignatura");
		btnChangeSubject.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelChangeSubject.add(btnChangeSubject);

        btnLogout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);			
			}
		});

        btnSaveScore.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateUserScore(Integer.parseInt(textID.getText()), 
				Float.parseFloat(textScore.getText()), 
                (SubjectData)subjectComboBox.getSelectedItem());
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				textID.setText("");
				textScore.setText("");
			}
		});
    }

    private void updateUserScore(Integer id, Float Score, SubjectData subject){

        UserData student = new UserData();
		ScoreData scoData = new ScoreData();
        scoData.setId(id);
        scoData.setScore(Score);
        scoData.setStudent(student);
        scoData.setSubject(subject);
	
		WebTarget registerUserWebTarget = webTarget.path("/scores" + textID.getText() + "/update")
			.queryParam("login", user.getLogin()).queryParam("password", user.getPassword());
		Invocation.Builder invocationBuilder = registerUserWebTarget.request(MediaType.APPLICATION_JSON);

		Response response = invocationBuilder.put(Entity.entity(scoData, MediaType.APPLICATION_JSON));
		if (response.getStatus() != Status.OK.getStatusCode()) {
			logger.error("Error connecting with the server. Code: {}", response.getStatus());
		} else {
			logger.info("User correctly updated");
		}
	}
}
