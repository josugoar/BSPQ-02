package es.deusto.spq.client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
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
import javax.swing.SwingConstants;
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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import es.deusto.spq.pojo.Role;
import es.deusto.spq.pojo.UserData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdminClient extends JFrame {

	protected static final Logger logger = LogManager.getLogger();
	private JPanel contentPane;
	private JTextField textEmail;
	private JTextField textName;
	private JTextField textLastName;
	private JTextField textPassword;
	private JTextField textNameInfo;
	private JTextField textLastNameInfo;
	private JTextField textEmailInfo;
	private JTextField textPasswordInfo;
	private JTextField textField_1;
	private JTable table;


	private Client client;
	private WebTarget webTarget;
	private UserData user;

	/**
	 * Create the frame.
	 */
	
	public AdminClient(UserData user, String hostname, String port) {
		this.user = user;
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		client = ClientBuilder.newClient();
		webTarget = client.target(String.format("http://%s:%s/rest/resource", hostname, port));

		this.addComponentListener(new ComponentAdapter() 
		{  
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
		
		JLabel lblDashboard = new JLabel("Panel de Control de Administrador");
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
		
		JPanel panelTitleLeft = new JPanel();
		panelTitleLeft.setBorder(UIManager.getBorder("DesktopIcon.border"));
		panelLeft.add(panelTitleLeft);
		panelTitleLeft.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblLeftTitle = new JLabel("Nuevo Usuario");
		lblLeftTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblLeftTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelTitleLeft.add(lblLeftTitle);
		
		JPanel panelEmail = new JPanel();
		panelLeft.add(panelEmail);
		
		JLabel lblEmail = new JLabel("Email         ");
		panelEmail.add(lblEmail);
		
		textEmail = new JTextField();
		panelEmail.add(textEmail);
		textEmail.setColumns(30);
		
		JPanel panePassword = new JPanel();
		panelLeft.add(panePassword);
		
		JLabel lblPassword = new JLabel("Contraseña");
		panePassword.add(lblPassword);
		
		textPassword = new JPasswordField();
		textPassword.setColumns(30);
		panePassword.add(textPassword);
		
		JPanel panelName = new JPanel();
		panelLeft.add(panelName);
		
		JLabel lblName = new JLabel("Nombre     ");
		panelName.add(lblName);
		
		textName = new JTextField();
		textName.setColumns(30);
		panelName.add(textName);
		
		JPanel panelLastName = new JPanel();
		panelLeft.add(panelLastName);
		
		JLabel lblLastName = new JLabel("Apellidos    ");
		panelLastName.add(lblLastName);
		
		textLastName = new JTextField();
		textLastName.setColumns(30);
		panelLastName.add(textLastName);
		
		JPanel panelAccountType = new JPanel();
		panelLeft.add(panelAccountType);
		
		JLabel lblAccountType = new JLabel("Tipo de Usuario");
		panelAccountType.add(lblAccountType);
		
		JComboBox<Role> comboAccountType = new JComboBox<>();
		comboAccountType.setModel(new DefaultComboBoxModel<Role>(Role.values()));
		panelAccountType.add(comboAccountType);
		
		JPanel panelCreateEditAccountBtns = new JPanel();
		panelCreateEditAccountBtns.setBorder(UIManager.getBorder("DesktopIcon.border"));
		panelLeft.add(panelCreateEditAccountBtns);
		panelCreateEditAccountBtns.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnCreateAccount = new JButton("Crear Usuario");
		btnCreateAccount.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelCreateEditAccountBtns.add(btnCreateAccount);
		
		JButton btnEditAccount = new JButton("Editar Usuario");
		btnEditAccount.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelCreateEditAccountBtns.add(btnEditAccount);
		btnEditAccount.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				updateUser(textEmail.getText(), 
				textPassword.getText(), 
				textName.getText(), 
				textLastName.getText(), 
				(Role)comboAccountType.getSelectedItem());

				update();
			}
		});
		btnCreateAccount.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				createUser(textEmail.getText(), 
				textPassword.getText(), 
				textName.getText(), 
				textLastName.getText(), 
				(Role)comboAccountType.getSelectedItem());

				update();

			}
		});
		
		JSplitPane splitPaneRight = new JSplitPane();
		splitPaneRight.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPaneRight);
		
		JPanel panelTable = new JPanel();
		splitPaneRight.setLeftComponent(panelTable);
		panelTable.setLayout(new BoxLayout(panelTable, BoxLayout.Y_AXIS));
		
		JPanel panelTableTitle = new JPanel();
		panelTableTitle.setBorder(UIManager.getBorder("DesktopIcon.border"));
		panelTable.add(panelTableTitle);
		
		JLabel lblTableTitle = new JLabel("Cuentas en la Base de Datos");
		lblTableTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelTableTitle.add(lblTableTitle);
		
		JPanel panelSeparatorRight = new JPanel();
		panelTable.add(panelSeparatorRight);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Email", "Contrase\u00F1a", "Nombre", "Apellidos", "Tipo"
			}
		) {
			Class<?>[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, Role.class
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

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				UserData user = new UserData();
				JTable table = (JTable)evt.getSource();
				int row = table.getSelectedRow();
				String email = (String)table.getValueAt(row, 0);
				user = getUser(email);
				textEmailInfo.setText(user.getLogin());
				textPasswordInfo.setText(user.getPassword());
				textNameInfo.setText(user.getName());
				textLastNameInfo.setText(user.getSurname());
				textField_1.setText(user.getRole().name());
				update();
			}
		});
		
		JScrollPane scrollTable = new JScrollPane(table);
		panelTable.add(scrollTable);
		
		JPanel panelAccountInfo = new JPanel();
		splitPaneRight.setRightComponent(panelAccountInfo);
		panelAccountInfo.setLayout(new BoxLayout(panelAccountInfo, BoxLayout.Y_AXIS));
		
		JPanel panelAccountInfoTitle = new JPanel();
		panelAccountInfoTitle.setBorder(UIManager.getBorder("DesktopIcon.border"));
		panelAccountInfo.add(panelAccountInfoTitle);
		panelAccountInfoTitle.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblAccountInfoTitle = new JLabel("Detalles de Usuario");
		lblAccountInfoTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblAccountInfoTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelAccountInfoTitle.add(lblAccountInfoTitle);
		
		JPanel panelNameAndLastNameInfo = new JPanel();
		panelAccountInfo.add(panelNameAndLastNameInfo);
		
		JLabel lblNameInfo = new JLabel("Nombre");
		panelNameAndLastNameInfo.add(lblNameInfo);
		
		textNameInfo = new JTextField();
		textNameInfo.setEditable(false);
		panelNameAndLastNameInfo.add(textNameInfo);
		textNameInfo.setColumns(10);
		
		JLabel lblLastNameInfo = new JLabel("Apellidos");
		panelNameAndLastNameInfo.add(lblLastNameInfo);
		
		textLastNameInfo = new JTextField();
		textLastNameInfo.setEditable(false);
		panelNameAndLastNameInfo.add(textLastNameInfo);
		textLastNameInfo.setColumns(30);
		
		JPanel panelEmailInfo = new JPanel();
		panelAccountInfo.add(panelEmailInfo);
		
		JLabel lblEmailInfo = new JLabel("Email   ");
		panelEmailInfo.add(lblEmailInfo);
		
		textEmailInfo = new JTextField();
		textEmailInfo.setEditable(false);
		panelEmailInfo.add(textEmailInfo);
		textEmailInfo.setColumns(46);
		
		JPanel panelPasswordInfo = new JPanel();
		panelAccountInfo.add(panelPasswordInfo);
		
		JLabel lblPasswordInfo = new JLabel("Contraseña");
		panelPasswordInfo.add(lblPasswordInfo);
		
		textPasswordInfo = new JPasswordField();
		textPasswordInfo.setEditable(false);
		textPasswordInfo.setColumns(44);
		panelPasswordInfo.add(textPasswordInfo);
		
		JPanel panelTypeInfo = new JPanel();
		panelAccountInfo.add(panelTypeInfo);
		
		JLabel lblTypeInfo = new JLabel("Tipo");
		panelTypeInfo.add(lblTypeInfo);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		panelTypeInfo.add(textField_1);
		
		JPanel panelDeleteAccount = new JPanel();
		panelAccountInfo.add(panelDeleteAccount);
		
		JButton btnDeleteAccount = new JButton("Eliminar Usuario");
		btnDeleteAccount.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelDeleteAccount.add(btnDeleteAccount);
		btnDeleteAccount.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				deleteUser(textEmailInfo.getText());
				update();
				
			}
		});
	}
	
	private void update() {

		DefaultTableModel myModel = (DefaultTableModel) table.getModel();
		myModel.setRowCount(0);
		List<UserData> users = getUsers();
		for (UserData user : users) {
			System.out.println(user.getLogin());
			Object[] data = {
				user.getLogin(),
				user.getPassword(),
				user.getName(),
				user.getSurname(),
				user.getRole(),
			};

			myModel.addRow(data);
        }
		revalidate();
		repaint();

	}

	private void createUser(String email, String password, String name, String surname, Role role){
		UserData currentUser = new UserData();
		currentUser.setLogin(email);
		currentUser.setPassword(password);
		currentUser.setName(name);
		currentUser.setSurname(surname);
		currentUser.setRole(role);
	
		WebTarget registerUserWebTarget = webTarget.path("users")
			.queryParam("login", user.getLogin()).queryParam("password", user.getPassword());
		Invocation.Builder invocationBuilder = registerUserWebTarget.request(MediaType.APPLICATION_JSON);

		Response response = invocationBuilder.post(Entity.entity(currentUser, MediaType.APPLICATION_JSON));
		if (response.getStatus() != Status.OK.getStatusCode()) {
			logger.error("Error connecting with the server. Code: {}", response.getStatus());
		} else {
			logger.info("User correctly registered");
		}

	}

	private void updateUser(String email, String password, String name, String surname, Role role){
		UserData currentUser = new UserData();
		currentUser.setLogin(email);
		currentUser.setPassword(password);
		currentUser.setName(name);
		currentUser.setSurname(surname);
		currentUser.setRole(role);
	
		WebTarget registerUserWebTarget = webTarget.path("users/" + email + "/update")
			.queryParam("login", user.getLogin()).queryParam("password", user.getPassword());
		Invocation.Builder invocationBuilder = registerUserWebTarget.request(MediaType.APPLICATION_JSON);

		Response response = invocationBuilder.put(Entity.entity(currentUser, MediaType.APPLICATION_JSON));
		if (response.getStatus() != Status.OK.getStatusCode()) {
			logger.error("Error connecting with the server. Code: {}", response.getStatus());
		} else {
			logger.info("User correctly updated");
		}
	}

	private void deleteUser(String email){
		WebTarget registerUserWebTarget = webTarget.path("users/" + email + "/delete")
			.queryParam("login", user.getLogin()).queryParam("password", user.getPassword());
		Invocation.Builder invocationBuilder = registerUserWebTarget.request(MediaType.APPLICATION_JSON);

		Response response = invocationBuilder.delete();
		if (response.getStatus() != Status.OK.getStatusCode()) {
			logger.error("Error connecting with the server. Code: {}", response.getStatus());
		} else {
			logger.info("User correctly updated");
		}
	}

	private List<UserData> getUsers(){
		WebTarget registerUserWebTarget = webTarget.path("users")
			.queryParam("login", user.getLogin()).queryParam("password", user.getPassword());
		Invocation.Builder invocationBuilder = registerUserWebTarget.request(MediaType.APPLICATION_JSON);

		Response response = invocationBuilder.get();
		if (response.getStatus() != Status.OK.getStatusCode()) {
			logger.error("Error connecting with the server. Code: {}", response.getStatus());
		} else {
			logger.info("User correctly updated");
		}
		return Arrays.asList(response.readEntity(UserData[].class));
	}

	private UserData getUser(String email) {
		WebTarget registerUserWebTarget = webTarget.path("users/" + email + "/get")
			.queryParam("login", user.getLogin()).queryParam("password", user.getPassword());
		Invocation.Builder invocationBuilder = registerUserWebTarget.request(MediaType.APPLICATION_JSON);

		Response response = invocationBuilder.get();
		if (response.getStatus() != Status.OK.getStatusCode()) {
			logger.error("Error connecting with the server. Code: {}", response.getStatus());
		} else {
			logger.info("User correctly updated");
		}
		return response.readEntity(UserData.class);
	}

}
