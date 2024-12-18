package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.imageio.ImageIO;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

import controller.CategoryController;
import dao.CategoryDAO;
import model.Category;
import model.User;
import util.ButtonHover;

public class CategoryManagementView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTextField searchField;
	private Timer searchTimer;
	private JTable table;
	private CategoryController categoryController;
	private CategoryDAO categoryDAO;

	/**
	 * Create the frame.
	 */
	public CategoryManagementView(User user) {
		categoryDAO = new CategoryDAO();
		categoryController = new CategoryController(categoryDAO, this);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				new Home(user);
			}
		});

		setResizable(false);
		setTitle("HaUI Grocery Store Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 840, 720);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(mainPanel);
		mainPanel.setLayout(new BorderLayout(0, 0));

		JLabel titleLabel = new JLabel("QUẢN LÝ LOẠI SẢN PHẨM", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		mainPanel.add(titleLabel, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		mainPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel headerPanel = new JPanel();
		panel.add(headerPanel, BorderLayout.NORTH);
		FlowLayout fl_headerPanel = new FlowLayout(FlowLayout.LEFT, 5, 5);
		headerPanel.setLayout(fl_headerPanel);

		JButton addBtn = new JButton("THÊM");
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CategoryView(categoryController, null, false);
			}
		});

		addBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		headerPanel.add(addBtn);
		addBtn.setOpaque(false);
		addBtn.setContentAreaFilled(false);
		addBtn.setBorderPainted(false);
		ButtonHover.addButtonHover(addBtn);

		JButton editBtn = new JButton("SỬA");
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Category category = getSelectedCategory();
				if (category == null) {
					JOptionPane.showMessageDialog(CategoryManagementView.this, "Vui lòng chọn loại sản phẩm", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				new CategoryView(categoryController, category, true);
			}
		});
		editBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		headerPanel.add(editBtn);
		editBtn.setOpaque(false);
		editBtn.setContentAreaFilled(false);
		editBtn.setBorderPainted(false);
		ButtonHover.addButtonHover(editBtn);

		JButton deleteBtn = new JButton("XÓA");
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Category category = getSelectedCategory();
				if (category == null) {
					JOptionPane.showMessageDialog(CategoryManagementView.this, "Vui lòng chọn loại sản phẩm", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				int option = JOptionPane.showConfirmDialog(CategoryManagementView.this,
						"Bạn có chắc chắn muốn xóa không?");

				if (option == 0) {
					try {
						if (!categoryController.deleteCategory(category)) {
							JOptionPane.showMessageDialog(CategoryManagementView.this, "Xóa thất bại", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					} catch (HeadlessException | ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}

					try {
						updateCategoryTable(categoryController.getAllCategories());
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(CategoryManagementView.this, "Xóa thành công");
				}
			}
		});
		deleteBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		headerPanel.add(deleteBtn);
		deleteBtn.setOpaque(false);
		deleteBtn.setContentAreaFilled(false);
		deleteBtn.setBorderPainted(false);

		ButtonHover.addButtonHover(deleteBtn);

		JButton detailBtn = new JButton("XEM CHI TIẾT");
		detailBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Category category = getSelectedCategory();
				if (category == null) {
					JOptionPane.showMessageDialog(CategoryManagementView.this, "Vui lòng chọn loại sản phẩm", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				new ProductByCategoryView(category);
			}
		});
		detailBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		headerPanel.add(detailBtn);
		detailBtn.setOpaque(false);
		detailBtn.setContentAreaFilled(false);
		detailBtn.setBorderPainted(false);

		ButtonHover.addButtonHover(detailBtn);

		JButton exportBtn = new JButton("XUẤT RA PDF");
		exportBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MessageFormat header = new MessageFormat(titleLabel.getText());
				MessageFormat footer = new MessageFormat("HaUI Grocery Store");
				try {
					PrintRequestAttributeSet set = new HashPrintRequestAttributeSet();
					set.add(OrientationRequested.PORTRAIT);
					table.print(JTable.PrintMode.FIT_WIDTH, header, footer, true, set, true);
					JOptionPane.showMessageDialog(null, "Print successfully");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		exportBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		headerPanel.add(exportBtn);
		exportBtn.setOpaque(false);
		exportBtn.setContentAreaFilled(false);
		exportBtn.setBorderPainted(false);
		ButtonHover.addButtonHover(exportBtn);

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Loại sản phẩm", "Mô tả" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(0).setMinWidth(40);
		table.getColumnModel().getColumn(0).setMaxWidth(40);
		table.setRowHeight(30);
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scrollPane, BorderLayout.CENTER);

		try {
			updateCategoryTable(categoryController.getAllCategories());
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void updateCategoryTable(List<Category> categories) {
		DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		dtm.setRowCount(0);
		categories.forEach(category -> {
			dtm.addRow(
					new Object[] { String.valueOf(category.getId()), category.getName(), category.getDescription() });
		});
	}

	public Category getSelectedCategory() {
		DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		int row = table.getSelectedRow();
		if (row == -1)
			return null;
		int id = Integer.valueOf(String.valueOf(dtm.getValueAt(row, 0)));
		try {
			Category category = categoryController.getCategoryById(id);
			return category;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
