package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.UserDAO;
import model.User;

public class VerifyUserView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTable table;
	private UserDAO userDAO;

	/**
	 * Create the frame.
	 */
	public VerifyUserView(User user) {
		userDAO = new UserDAO();
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 720);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(mainPanel);
		mainPanel.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("Xác minh tài khoản", SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		mainPanel.add(lblNewLabel, BorderLayout.NORTH);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Username", "Email", "Tr\u1EA1ng th\u00E1i" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(0).setMinWidth(30);
		table.getColumnModel().getColumn(0).setMaxWidth(30);
		table.setRowHeight(30);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				int id = Integer.valueOf(String.valueOf(table.getValueAt(row, 0)));
				try {
					User selectedUser = userDAO.get(u -> u.getId() == id);

					if (selectedUser != null) {
						int option = JOptionPane.showConfirmDialog(VerifyUserView.this,
								"Bạn đồng ý xác minh cho tài khoản này?", "Xác minh tài khoản",
								JOptionPane.YES_NO_OPTION);
						if (option == JOptionPane.YES_OPTION) {
							selectedUser.setVerify(true);
							userDAO.update(selectedUser);
							getData();
						}
					}
					getData();
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		getData();

		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void getData() {
		try {
			List<User> users = userDAO.getAll();
			DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
			dtm.setRowCount(0);
			users.forEach(user -> {
				if (!user.isVerify()) {
					dtm.addRow(new Object[] { String.valueOf(user.getId()), user.getUsername(), user.getEmail(),
							"Chưa xác minh" });
				}
			});
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

}
