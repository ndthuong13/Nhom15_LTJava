package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.UserController;
import dao.UserDAO;
import model.Bill;
import model.Product;
import model.User;

public class BillDetailView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTable table;
	private UserDAO userDAO;
	private UserController userController;

	/**
	 * Create the frame.
	 */
	public BillDetailView(Bill bill) {
		userDAO = new UserDAO();
		userController = new UserController(userDAO);

		this.setIconImage(
				Toolkit.getDefaultToolkit().getImage(BillDetailView.class.getResource("/resources/bill-icon.png")));
		HashMap<Product, Integer> products = bill.getProducts();
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 720);
		mainPanel = new JPanel();
		mainPanel.setForeground(new Color(255, 255, 255));
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(mainPanel);
		mainPanel.setLayout(null);

		JLabel titleLabel = new JLabel("HÓA ĐƠN", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		titleLabel.setBounds(10, 0, 414, 55);
		mainPanel.add(titleLabel);

		JLabel idLabel = new JLabel("Mã hóa đơn");
		idLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		idLabel.setBounds(20, 56, 136, 32);
		mainPanel.add(idLabel);

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "ID", "T\u00EAn s\u1EA3n ph\u1EA9m",
				"\u0110\u01A1n gi\u00E1", "S\u1ED1 l\u01B0\u1EE3ng mua" }));
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setMinWidth(50);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setMinWidth(80);
		table.getColumnModel().getColumn(2).setMaxWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setMinWidth(80);
		table.getColumnModel().getColumn(3).setMaxWidth(100);
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		table.setRowHeight(30);

		DefaultTableModel dtm = (DefaultTableModel) table.getModel();
		for (Product product : products.keySet()) {
			dtm.addRow(new Object[] { product.getId(), product.getName(), String.valueOf(product.getPrice()),
					products.get(product) });
		}

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 216, 464, 411);
		mainPanel.add(scrollPane);

		JLabel tableLabel = new JLabel("Danh sách sản phẩm");
		tableLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tableLabel.setBounds(20, 184, 146, 32);
		mainPanel.add(tableLabel);

		JLabel lblHTnKhch = new JLabel("Họ tên khách hàng");
		lblHTnKhch.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHTnKhch.setBounds(20, 88, 136, 32);
		mainPanel.add(lblHTnKhch);

		JLabel adminLabel = new JLabel("Người tạo");
		adminLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		adminLabel.setBounds(20, 120, 136, 32);
		mainPanel.add(adminLabel);

		JLabel dateLabel = new JLabel("Ngày tạo");
		dateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		dateLabel.setBounds(20, 152, 136, 32);
		mainPanel.add(dateLabel);

		JLabel billIdLabel = new JLabel(bill.getId());
		billIdLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		billIdLabel.setBounds(166, 56, 258, 32);
		mainPanel.add(billIdLabel);

		JLabel customerNameLabel = new JLabel(bill.getName());
		customerNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		customerNameLabel.setBounds(166, 88, 258, 32);
		mainPanel.add(customerNameLabel);

		try {
			User user = userController.getUserById(bill.getAdminId());
			JLabel adminNameLabel = new JLabel(user.getUsername());
			adminNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
			adminNameLabel.setBounds(166, 120, 258, 32);
			mainPanel.add(adminNameLabel);
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}

		JLabel billDateLabel = new JLabel(bill.getDate());
		billDateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		billDateLabel.setBounds(166, 152, 258, 32);
		mainPanel.add(billDateLabel);

		JLabel priceLabel = new JLabel("Tổng tiền");
		priceLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		priceLabel.setBounds(216, 638, 74, 32);
		mainPanel.add(priceLabel);

		JLabel billTotalLabel = new JLabel(String.valueOf(bill.getTotal()));
		billTotalLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		billTotalLabel.setBounds(377, 638, 97, 32);
		mainPanel.add(billTotalLabel);

		setLocationRelativeTo(null);
		setVisible(true);
	}

}
