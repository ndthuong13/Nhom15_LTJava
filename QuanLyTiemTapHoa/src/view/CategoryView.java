package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controller.CategoryController;
import model.Category;
import util.FormUtils;
import java.awt.Toolkit;

public class CategoryView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTextField nameField;
	private JTextField desField;
	private JTextField idField;

	public CategoryView(CategoryController categoryController, Category category, boolean isEdit) {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 650, 400);
		mainPanel = new JPanel();
		mainPanel.setForeground(new Color(255, 255, 255));
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(mainPanel);
		mainPanel.setLayout(null);

		String title = (isEdit ? "SỬA" : "THÊM").concat(" LOẠI SẢN PHẨM");
		JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		titleLabel.setBounds(107, 0, 414, 55);
		mainPanel.add(titleLabel);

		JLabel nameLabel = new JLabel("Loại sản phẩm");
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameLabel.setBounds(252, 53, 120, 26);
		mainPanel.add(nameLabel);

		JLabel desLabel = new JLabel("Mô tả");
		desLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		desLabel.setBounds(67, 131, 120, 26);
		mainPanel.add(desLabel);

		nameField = new JTextField(isEdit ? category.getName() : "");
		nameField.setFont(new Font("Dialog", Font.PLAIN, 14));
		nameField.setBounds(252, 78, 300, 33);
		mainPanel.add(nameField);
		nameField.setColumns(10);
		nameField.setBorder(
				BorderFactory.createCompoundBorder(nameField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		desField = new JTextField(isEdit ? category.getDescription() : "");
		desField.setFont(new Font("Dialog", Font.PLAIN, 14));
		desField.setColumns(10);
		desField.setBounds(67, 157, 485, 101);
		mainPanel.add(desField);
		desField.setBorder(
				BorderFactory.createCompoundBorder(desField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		idField = new JTextField(isEdit ? String.valueOf(category.getId()) : "");
		idField.setFont(new Font("Dialog", Font.PLAIN, 14));
		idField.setColumns(10);
		idField.setBorder(
				BorderFactory.createCompoundBorder(idField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		idField.setBounds(67, 78, 152, 33);
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
					JOptionPane.showMessageDialog(CategoryView.this, "Vui lòng nhập đầy đủ thông tin", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				int id = Integer.valueOf(idField.getText());
				String name = nameField.getText();
				String des = desField.getText();

				if (!isEdit) {
					try {
						if (!categoryController.addCategory(new Category(id, name, des))) {
							JOptionPane.showMessageDialog(CategoryView.this, "Thêm thất bại", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					} catch (HeadlessException | ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(CategoryView.this, "Thêm thành công");
				} else {
					category.setId(id);
					category.setName(name);
					category.setDescription(des);
					try {
						if (!categoryController.updateCategory(category)) {
							JOptionPane.showMessageDialog(CategoryView.this, "Sửa thất bại", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					} catch (HeadlessException | ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(CategoryView.this, "Sửa thành công");
				}

				FormUtils.resetForm(mainPanel);
				dispose();
			}
		});
		saveBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		saveBtn.setBounds(271, 288, 89, 33);
		mainPanel.add(saveBtn);

		setLocationRelativeTo(null);
		setVisible(true);
	}
}
