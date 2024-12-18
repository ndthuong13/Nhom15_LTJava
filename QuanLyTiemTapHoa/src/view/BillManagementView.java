package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import controller.BillController;
import controller.UserController;
import dao.BillDAO;
import dao.UserDAO;
import model.Bill;
import model.User;
import util.ButtonHover;

public class BillManagementView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTextField searchField;
	private JComboBox<String> searchOptionComboBox;
	private JTable table;
	private BillController billController;
	private BillDAO billDAO;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BillManagementView(User user) {
		billDAO = new BillDAO();
		billController = new BillController(billDAO, this);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				new Home(user);
			}
		});

		setResizable(false);
		setTitle("Quản lý tiệm tạp hoá Xanh");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 840, 720);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(mainPanel);
		mainPanel.setLayout(new BorderLayout(0, 0));

		JLabel titleLabel = new JLabel("QUẢN LÝ HÓA ĐƠN", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		mainPanel.add(titleLabel, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		mainPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel headerPanel = new JPanel();
		panel.add(headerPanel, BorderLayout.NORTH);
		FlowLayout fl_headerPanel = new FlowLayout(FlowLayout.LEFT, 5, 5);
		headerPanel.setLayout(fl_headerPanel);

		JButton detailBtn = new JButton("XEM CHI TIẾT");
		detailBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Bill bill = getSelectedBill();
				if (bill == null) {
					JOptionPane.showMessageDialog(BillManagementView.this, "Vui lòng chọn hoá đơn", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				new BillDetailView(bill);
			}
		});
		detailBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		headerPanel.add(detailBtn);
		detailBtn.setOpaque(false);
		detailBtn.setContentAreaFilled(false);
		detailBtn.setBorderPainted(false);
		try {
			Image img = ImageIO.read(getClass().getResource("/resources/detail-icon.png"));
			Image sizedImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			detailBtn.setIcon(new ImageIcon(sizedImg));
			detailBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
			detailBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		} catch (Exception e) {
			System.out.println(e);
		}
		ButtonHover.addButtonHover(detailBtn);

		JLabel lblNewLabel = new JLabel("                                                             ");
		headerPanel.add(lblNewLabel);

		JPanel searchPanel = new JPanel();
		headerPanel.add(searchPanel);
		searchPanel.setLayout(new BorderLayout(0, 0));

		Timer searchTimer = new Timer(200, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String keyword = searchField.getText();
				if (keyword.equals(""))
					try {
						updateBillTable(billController.getAllBills());
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
				int searchOption = searchOptionComboBox.getSelectedIndex();
				if (searchOption == 0)
					return;
				billController.searchBills(getDataFromTable(), keyword, searchOption);
			}
		});
		searchTimer.setRepeats(false);

		searchField = new JTextField();
		searchField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		searchPanel.add(searchField);
		searchField.setColumns(20);
		searchField.setBorder(BorderFactory.createCompoundBorder(searchField.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				restartTimer();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				restartTimer();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				restartTimer();
			}

			private void restartTimer() {
				if (searchTimer.isRunning()) {
					searchTimer.restart();
				} else {
					searchTimer.start();
				}
			}
		});

		JLabel searchLabel = new JLabel("Tìm kiếm");
		searchLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		searchPanel.add(searchLabel, BorderLayout.NORTH);

		searchOptionComboBox = new JComboBox<String>();
		searchPanel.add(searchOptionComboBox, BorderLayout.EAST);
		searchOptionComboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		searchOptionComboBox.setModel(new DefaultComboBoxModel(new String[] { "", "Mã hóa đơn", "Ngày tạo" }));

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "M\u00E3 h\u00F3a \u0111\u01A1n", "T\u00EAn kh\u00E1ch h\u00E0ng",
						"Ng\u01B0\u1EDDi t\u1EA1o", "Ng\u00E0y t\u1EA1o", "T\u1ED5ng ti\u1EC1n" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(0).setMaxWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(1).setMinWidth(200);
		table.getColumnModel().getColumn(1).setMaxWidth(200);
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
		table.getColumnModel().getColumn(2).setMinWidth(200);
		table.getColumnModel().getColumn(2).setMaxWidth(200);
		table.setRowHeight(30);
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scrollPane, BorderLayout.CENTER);

		try {
			updateBillTable(billController.getAllBills());
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private List<Bill> getDataFromTable() {
		List<Bill> bills = new ArrayList<>();
		DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		int rowCount = dtm.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			String id = String.valueOf(dtm.getValueAt(i, 0));
			try {
				Bill bill = billController.getBillById(id);
				bills.add(bill);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		return bills;
	}

	public void updateBillTable(List<Bill> bills) {
		DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		dtm.setRowCount(0);
		UserDAO userDao = new UserDAO();
		UserController userController = new UserController(userDao);
		bills.forEach(bill -> {
			try {
				User user = userController.getUserById(bill.getAdminId());
				dtm.addRow(new Object[] { bill.getId(), bill.getName(), user.getUsername(), bill.getDate(),
						bill.getTotal() });
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		});
	}

	public Bill getSelectedBill() {
		DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		int row = table.getSelectedRow();
		if (row == -1)
			return null;
		String id = String.valueOf(dtm.getValueAt(row, 0));
		try {
			Bill bill = billController.getBillById(id);
			return bill;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
