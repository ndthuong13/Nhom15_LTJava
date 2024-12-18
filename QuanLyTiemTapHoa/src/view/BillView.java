package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.BillController;
import controller.ProductController;
import dao.ProductDAO;
import model.Bill;
import model.Product;
import model.User;
import service.ProductSelection;
import util.BillIdGenerator;
import util.FormUtils;

public class BillView extends JFrame implements ProductSelection {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTextField nameField;
	private JTable table;
	private ProductDAO productDAO;
	private ProductController productController;

	/**
	 * Create the frame.
	 */
	public BillView(BillController billController, User user) {
		productDAO = new ProductDAO();
		productController = new ProductController(productDAO, null);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				new Home(user);
			}
		});

		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 720);
		mainPanel = new JPanel();
		mainPanel.setForeground(new Color(255, 255, 255));
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(mainPanel);
		mainPanel.setLayout(null);

		JLabel titleLabel = new JLabel("TẠO HÓA ĐƠN", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		titleLabel.setBounds(10, 0, 414, 55);
		mainPanel.add(titleLabel);

		nameField = new JTextField();
		nameField.setFont(new Font("Dialog", Font.PLAIN, 14));
		nameField.setColumns(10);
		nameField.setBorder(
				BorderFactory.createCompoundBorder(nameField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		nameField.setBounds(15, 81, 234, 33);
		mainPanel.add(nameField);

		JLabel nameLabel = new JLabel("Tên khách hàng");
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameLabel.setBounds(15, 53, 120, 26);
		mainPanel.add(nameLabel);

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Tên sản phẩm",
				"Đơn giá", "Số lượng mua" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 140, 414, 486);
		mainPanel.add(scrollPane);

		JButton saveBtn = new JButton("Tạo đơn hàng");
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!FormUtils.ValidateForm(mainPanel)) {
					JOptionPane.showMessageDialog(BillView.this, "Vui lòng nhập đầy đủ thông tin", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				String id;
				while (true) {
					id = BillIdGenerator.generateBillId();
					try {
						if (billController.getBillById(id) == null)
							break;
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
				}

				String name = nameField.getText();
				int adminId = user.getId();
				Date date = new Date();

				HashMap<Product, Integer> products = new HashMap<Product, Integer>();
				DefaultTableModel dtm = (DefaultTableModel) table.getModel();
				int rowCount = dtm.getRowCount();
				for (int i = 0; i < rowCount; i++) {
					int productId = Integer.parseInt(dtm.getValueAt(i, 0).toString());
					try {
						Product product = productController.getProductById(productId);
						int buyQuantity = Integer.valueOf(String.valueOf(dtm.getValueAt(i, 3)));
						products.put(product, buyQuantity);
						product.setQuantity(product.getQuantity() - buyQuantity);
						productController.updateProduct(product);
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
				}

				try {
					if (billController.addBill(new Bill(id, name, adminId, date, products))) {
						JOptionPane.showMessageDialog(BillView.this, "Tạo hóa đơn thành công");
						dispose();
						new Home(user);
					} else {
						JOptionPane.showMessageDialog(BillView.this, "Tạo hóa đơn thất bại", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}

				FormUtils.resetForm(mainPanel);
			}
		});
		saveBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		saveBtn.setBounds(146, 636, 137, 33);
		mainPanel.add(saveBtn);

		JButton addProductBtn = new JButton("Thêm sản phẩm");
		addProductBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ProductManagementView(true, BillView.this);
			}
		});
		addProductBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		addProductBtn.setBounds(259, 81, 165, 33);
		mainPanel.add(addProductBtn);

		JLabel tableLabel = new JLabel("Danh sách sản phẩm");
		tableLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tableLabel.setBounds(10, 115, 146, 26);
		mainPanel.add(tableLabel);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void onProductSelected(Product selectedProduct, int quantity) {
		DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		dtm.addRow(new Object[] { selectedProduct.getId(), selectedProduct.getName(), selectedProduct.getPrice(),
				String.valueOf(quantity) });
	}
}
