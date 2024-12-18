package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controller.CategoryController;
import controller.ProductController;
import dao.CategoryDAO;
import model.Category;
import model.Product;
import util.FormUtils;
import javax.swing.ImageIcon;
import java.awt.Toolkit;

public class ProductView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTextField nameField;
	private JTextField priceField;
	private JTextField idField;
	private JTextField quantityField;
	private JComboBox<Category> categoryComboBox;
	private CategoryDAO categoryDao;
	private CategoryController categoryController;
	
	public ProductView(ProductController productController, Product product, boolean isEdit) {
		categoryDao = new CategoryDAO();
		categoryController = new CategoryController(categoryDao, null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 620, 350);
		mainPanel = new JPanel();
		mainPanel.setForeground(new Color(255, 255, 255));
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(mainPanel);
		mainPanel.setLayout(null);

		String title = (isEdit ? "SỬA" : "THÊM").concat(" LOẠI SẢN PHẨM");
		JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		titleLabel.setBounds(91, 0, 414, 55);
		mainPanel.add(titleLabel);

		JLabel nameLabel = new JLabel("Tên sản phẩm");
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameLabel.setBounds(251, 53, 120, 26);
		mainPanel.add(nameLabel);

		JLabel priceLabel = new JLabel("Giá");
		priceLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		priceLabel.setBounds(251, 121, 120, 26);
		mainPanel.add(priceLabel);

		nameField = new JTextField(isEdit ? product.getName() : "");
		nameField.setFont(new Font("Dialog", Font.PLAIN, 14));
		nameField.setBounds(251, 78, 300, 33);
		mainPanel.add(nameField);
		nameField.setColumns(10);
		nameField.setBorder(
				BorderFactory.createCompoundBorder(nameField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		priceField = new JTextField(isEdit ? String.valueOf(product.getPrice()) : "");
		priceField.setFont(new Font("Dialog", Font.PLAIN, 14));
		priceField.setColumns(10);
		priceField.setBounds(251, 142, 153, 33);
		mainPanel.add(priceField);
		priceField.setBorder(BorderFactory.createCompoundBorder(priceField.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		idField = new JTextField(isEdit ? String.valueOf(product.getId()) : "");
		idField.setFont(new Font("Dialog", Font.PLAIN, 14));
		idField.setColumns(10);
		idField.setBorder(
				BorderFactory.createCompoundBorder(idField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		idField.setBounds(67, 78, 153, 33);
		mainPanel.add(idField);

		JLabel idLabel = new JLabel("Mã sản phẩm");
		idLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		idLabel.setBounds(67, 53, 120, 26);
		mainPanel.add(idLabel);

		if (isEdit)
			idField.setEnabled(false);

		JButton saveBtn = new JButton("LƯU");
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!FormUtils.ValidateForm(mainPanel)) {
					JOptionPane.showMessageDialog(ProductView.this, "Vui lòng nhập đầy đủ thông tin", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				int id = Integer.valueOf(idField.getText());
				String name = nameField.getText();
				Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
				int categoryId = selectedCategory.getId();
				double price = Float.valueOf(priceField.getText());
				int quantity = Integer.valueOf(quantityField.getText());

				if (!isEdit) {
					try {
						if (!productController.addProduct(new Product(id, name, categoryId, price, quantity))) {
							JOptionPane.showMessageDialog(ProductView.this, "Thêm thất bại", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					} catch (HeadlessException | ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(ProductView.this, "Thêm thành công");
				} else {
					product.setId(id);
					product.setName(name);
					product.setCategoryId(categoryId);
					product.setPrice(price);
					product.setQuantity(quantity);
					try {
						if (!productController.updateProduct(product)) {
							JOptionPane.showMessageDialog(ProductView.this, "Sửa thất bại", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					} catch (HeadlessException | ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(ProductView.this, "Sửa thành công");
				}

				FormUtils.resetForm(mainPanel);
				dispose();
			}
		});
		saveBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		saveBtn.setBounds(251, 213, 89, 33);
		mainPanel.add(saveBtn);

		JLabel quantityLabel = new JLabel("Số lượng");
		quantityLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		quantityLabel.setBounds(414, 121, 120, 26);
		mainPanel.add(quantityLabel);

		quantityField = new JTextField(isEdit ? String.valueOf(product.getQuantity()) : "");
		quantityField.setFont(new Font("Dialog", Font.PLAIN, 14));
		quantityField.setColumns(10);
		quantityField.setBorder(BorderFactory.createCompoundBorder(quantityField.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		quantityField.setBounds(414, 142, 137, 33);
		mainPanel.add(quantityField);

		JLabel categoryLabel = new JLabel("Loại sản phẩm");
		categoryLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		categoryLabel.setBounds(67, 121, 120, 26);
		mainPanel.add(categoryLabel);

		categoryComboBox = new JComboBox<Category>();
		try {
			List<Category> categories = categoryController.getAllCategories();
			DefaultComboBoxModel<Category> comboBoxModel = new DefaultComboBoxModel<Category>();
			for (Category category : categories) {
				comboBoxModel.addElement(category);
			}
			categoryComboBox.setModel(comboBoxModel);
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
		if (isEdit)
			try {
				categoryComboBox.setSelectedItem(categoryController.getCategoryById(product.getCategoryId()));
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
		categoryComboBox.setBounds(67, 144, 153, 33);
		mainPanel.add(categoryComboBox);

		setLocationRelativeTo(null);
		setVisible(true);
	}
}
