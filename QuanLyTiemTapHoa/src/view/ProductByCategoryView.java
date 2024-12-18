package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.ProductController;
import dao.ProductDAO;
import model.Category;
import model.Product;

public class ProductByCategoryView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTable table;
	int categoryId;
	private ProductDAO productDAO;
	private ProductController productController;

	/**
	 * Create the frame.
	 */
	public ProductByCategoryView(Category category) {
		categoryId = category.getId();
		productDAO = new ProductDAO();
		productController = new ProductController(productDAO, null);
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 720);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(mainPanel);
		mainPanel.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel(category.getName(), SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		mainPanel.add(lblNewLabel, BorderLayout.NORTH);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Tên sẩn phẩm", "Giá", "Số lượng" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(0).setMinWidth(30);
		table.getColumnModel().getColumn(0).setMaxWidth(30);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(2).setMaxWidth(160);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setMinWidth(80);
		table.getColumnModel().getColumn(3).setMaxWidth(80);
		table.setRowHeight(30);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		getData();

		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void getData() {
		try {
			List<Product> products = productController.getAllProducts();
			DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
			dtm.setRowCount(0);
			products.forEach(product -> {
				if (product.getCategoryId() == categoryId) {
					dtm.addRow(new Object[] { String.valueOf(product.getId()), product.getName(),
							String.valueOf(product.getPrice()), String.valueOf(product.getQuantity()) });
				}
			});
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

}
