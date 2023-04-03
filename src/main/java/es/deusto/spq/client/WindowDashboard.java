package es.deusto.spq.client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;

public class WindowDashboard extends JFrame {

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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowDashboard frame = new WindowDashboard();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	private enum accountType {
		ESTUDIANTE,
		PROFESOR,
		DECANO,
		DIRECTOR
	}
	
	public WindowDashboard() {
		
		
		
		
		
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
				// TODO Ocultar o cerrar el panel de admin y volver a Login
				
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
		
		textPassword = new JTextField();
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
		
		JComboBox comboAccountType = new JComboBox();
		comboAccountType.setModel(new DefaultComboBoxModel(accountType.values()));
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
		btnCreateAccount.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Llamar al endpoint POST para crear una cuenta
				
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
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
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
		
		textPasswordInfo = new JTextField();
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
	}
	
	void update() {
		/*
		 * TODO Actualizar modelo de taba y contenido del panel de 
		 * informacion cuando se selecciona una fila en la tabla
		 */
	}

}
