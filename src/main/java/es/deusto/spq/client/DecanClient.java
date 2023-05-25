package es.deusto.spq.client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.FlowLayout;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import es.deusto.spq.pojo.ScoreData;
import es.deusto.spq.pojo.UserData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DecanClient extends JFrame {
	private JPanel contentPane;
	private JTextField tfAsignatura;
	private JTable table;

	private Client client;
	private WebTarget webTarget;
	protected static final Logger logger = LogManager.getLogger();
	private UserData user;
	private List<ScoreData> scores;
	private JTextField tfSistemasGrade;
	private JTextField tfPotenciaGrade;

	public DecanClient(UserData user, String hostname, String port) {
		this.user = user;
		client = ClientBuilder.newClient();
		webTarget = client.target(String.format("http://%s:%s/rest/resource", hostname, port));

		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				update();
			}
		});

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

		JLabel lblDashboard = new JLabel("DECANO");
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

		JLabel lblAsignatura = new JLabel("Nombre de la asignatura");
		panelEmail.add(lblAsignatura);

		tfAsignatura = new JTextField();
		panelEmail.add(tfAsignatura);
		tfAsignatura.setColumns(30);

		JPanel panelCreateEditAccountBtns = new JPanel();
		panelCreateEditAccountBtns.setBorder(UIManager.getBorder("DesktopIcon.border"));
		panelLeft.add(panelCreateEditAccountBtns);
		panelCreateEditAccountBtns.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton btnSee = new JButton("Ver");
		btnSee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnSee.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelCreateEditAccountBtns.add(btnSee);

		JButton btnCreate = new JButton("Crear");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCreate.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelCreateEditAccountBtns.add(btnCreate);

		JSplitPane splitPaneRight = new JSplitPane();
		splitPaneRight.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPaneRight);

		JPanel panelTable = new JPanel();
		splitPaneRight.setLeftComponent(panelTable);
		panelTable.setLayout(new BoxLayout(panelTable, BoxLayout.Y_AXIS));

		JPanel panelTableTitle = new JPanel();
		panelTableTitle.setBorder(UIManager.getBorder("DesktopIcon.border"));
		panelTable.add(panelTableTitle);

		JLabel lblTableTitle = new JLabel("Calificaciones generales de la facultad");
		lblTableTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelTableTitle.add(lblTableTitle);

		table = new JTable();
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Asignatura", "Estudiante", "Calificación"
				}) {
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

		JPanel panelMeanGrade = new JPanel();
		splitPaneRight.setRightComponent(panelMeanGrade);

		JLabel lblSistemasGrade = new JLabel("Nota media de \"Sistemas\":");
		panelMeanGrade.add(lblSistemasGrade);

		tfSistemasGrade = new JTextField();
		tfSistemasGrade.setEditable(false);
		panelMeanGrade.add(tfSistemasGrade);
		tfSistemasGrade.setColumns(10);

		JLabel lblPotenciaGrade = new JLabel("Nota media de \"Potencia\":");
		panelMeanGrade.add(lblPotenciaGrade);

		tfPotenciaGrade = new JTextField();
		tfPotenciaGrade.setEditable(false);
		tfPotenciaGrade.setColumns(10);
		panelMeanGrade.add(tfPotenciaGrade);

		btnLogout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}
		});

		btnSee.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});

		btnCreate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
	}

	private void update() {

		DefaultTableModel myModel = (DefaultTableModel) table.getModel();
		myModel.setRowCount(0);
		scores = getScores();
		for (ScoreData score : scores) {
			System.out.println(user.getLogin());
			Object[] data = {
					score.getId(),
					score.getSubject().getName(),
					score.getStudent().getLogin(),
					score.getScore()
			};

			myModel.addRow(data);
		}
		System.out.println(scores);
		revalidate();
		repaint();

	}

	private List<ScoreData> getScores() {

		WebTarget getScoresWebTarget = webTarget.path("scores")
				.queryParam("login", user.getLogin()).queryParam("password", user.getPassword());
		Invocation.Builder invocationBuilder = getScoresWebTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		if (response.getStatus() != Status.OK.getStatusCode()) {
			logger.error("Error connecting with the server. Code: {}", response.getStatus());
		} else {
			logger.info("Scores correctly listed");
		}
		return Arrays.asList(response.readEntity(ScoreData[].class));
	}
}