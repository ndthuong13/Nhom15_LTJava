package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
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

import controller.CategoryController;
import controller.ProductController;
import dao.CategoryDAO;
import dao.ProductDAO;
import model.Product;
import model.User;
import service.ProductSelection;
import util.ButtonHover;

public class ProductManagementView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTextField searchField;
	private Timer searchTimer;
	private JTable table;
	private JComboBox<String> sortComboBox;
	private ProductController productController;
	private ProductDAO productDAO;
	private CategoryDAO categoryDAO;
	private CategoryController categoryController;
	private ProductSelection ps;

	public ProductManagementView(User user) {
		this(false, null);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				new Home(user);
			}
		});
	}

	/**
	 * @wbp.parser.constructor
	 */
	public ProductManagementView(boolean isBuying, ProductSelection listener) {
		this.ps = listener;
		productDAO = new ProductDAO();
		categoryDAO = new CategoryDAO();
		categoryController = new CategoryController(categoryDAO, null);
		productController = new ProductController(productDAO, this);

		setResizable(false);
		setTitle("HaUI Grocery Store Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 840, 720);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(mainPanel);
		mainPanel.setLayout(new BorderLayout(0, 0));

		JLabel titleLabel = new JLabel("QUẢN LÝ SẢN PHẨM", SwingConstants.CENTER);
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
				new ProductView(productController, null, false);
			}
		});

		addBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		headerPanel.add(addBtn);
		addBtn.setOpaque(false);
		addBtn.setContentAreaFilled(false);
		addBtn.setBorderPainted(false);
		try {
			Image img = ImageIO.read(getClass().getResource("/resources/add-icon.png"));
			Image sizedImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			addBtn.setIcon(new ImageIcon(sizedImg));
			addBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
			addBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		} catch (Exception e) {
			System.out.println(e);
		}
		ButtonHover.addButtonHover(addBtn);

		JButton editBtn = new JButton("SỬA");
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Product product = getSelectedProduct();
				if (product == null) {
					JOptionPane.showMessageDialog(ProductManagementView.this, "Vui lòng chọn loại sản phẩm", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				new ProductView(productController, product, true);
			}
		});
		editBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		headerPanel.add(editBtn);
		editBtn.setOpaque(false);
		editBtn.setContentAreaFilled(false);
		editBtn.setBorderPainted(false);
		try {
			Image img = ImageIO.read(getClass().getResource("/resources/edit-icon.png"));
			Image sizedImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			editBtn.setIcon(new ImageIcon(sizedImg));
			editBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
			editBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		} catch (Exception e) {
			System.out.println(e);
		}
		ButtonHover.addButtonHover(editBtn);

		JButton deleteBtn = new JButton("XÓA");
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Product product = getSelectedProduct();
				if (product == null) {
					JOptionPane.showMessageDialog(ProductManagementView.this, "Vui lòng chọn loại sản phẩm", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				int option = JOptionPane.showConfirmDialog(ProductManagementView.this,
						"Bạn có chắc chắn muốn xóa không?");

				if (option == 0) {
					try {
						if (!productController.deleteProduct(product)) {
							JOptionPane.showMessageDialog(ProductManagementView.this, "Xóa thất bại", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					} catch (HeadlessException | ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}

					try {
						updateProductTable(productController.getAllProducts());
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(ProductManagementView.this, "Xóa thành công");
				}
			}
		});
		deleteBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		headerPanel.add(deleteBtn);
		deleteBtn.setOpaque(false);
		deleteBtn.setContentAreaFilled(false);
		deleteBtn.setBorderPainted(false);
		try {
			Image img = ImageIO.read(getClass().getResource("/resources/delete-icon.png"));
			Image sizedImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			deleteBtn.setIcon(new ImageIcon(sizedImg));
			deleteBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
			deleteBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		} catch (Exception e) {
			System.out.println(e);
		}
		ButtonHover.addButtonHover(deleteBtn);

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
		try {
			Image img = ImageIO.read(getClass().getResource("/resources/export-to-pdf-icon.png"));
			Image sizedImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			exportBtn.setIcon(new ImageIcon(sizedImg));
			exportBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
			exportBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		} catch (Exception e) {
			System.out.println(e);
		}
		ButtonHover.addButtonHover(exportBtn);

		
		JPanel sortPanel = new JPanel();
		headerPanel.add(sortPanel);
		sortPanel.setLayout(new BorderLayout(0, 0));

		JLabel sortLabel = new JLabel("Sắp xếp theo giá");
		sortLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		sortPanel.add(sortLabel, BorderLayout.NORTH);

		sortComboBox = new JComboBox<String>();
		sortComboBox
				.setModel(new DefaultComboBoxModel<String>(new String[] { "Không sắp xếp", "Tăng dần", "Giảm dần" }));
		sortComboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		sortComboBox.setBorder(BorderFactory.createCompoundBorder(sortComboBox.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		sortPanel.add(sortComboBox, BorderLayout.SOUTH);

		sortComboBox.addActionListener(new ActionListener() {
			private Timer sortTimer = new Timer(500, e -> {
				int choose = sortComboBox.getSelectedIndex();
				if (choose == 0)
					return;
				boolean isINC = choose == 1 ? true : false;
				productController.sortProductsByPrice(getDataFromTable(), isINC);
			});

			@Override
			public void actionPerformed(ActionEvent e) {
				if (sortTimer.isRunning()) {
					sortTimer.restart();
				} else {
					sortTimer.start();
				}
			}
		});

		table = new JTable() {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Tên sản phẩm",
				"Loại sản phẩm", "Đơn giá", "Số lượng" }));
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(0).setMinWidth(40);
		table.getColumnModel().getColumn(0).setMaxWidth(40);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setMinWidth(100);
		table.getColumnModel().getColumn(3).setMaxWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setMinWidth(100);
		table.getColumnModel().getColumn(4).setMaxWidth(100);
		table.setRowHeight(30);
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scrollPane, BorderLayout.CENTER);

		try {
			updateProductTable(productController.getAllProducts());
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		if (isBuying) {
			addBtn.setVisible(false);
			editBtn.setVisible(false);
			deleteBtn.setVisible(false);
			exportBtn.setVisible(false);

			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					DefaultTableModel dtm = (DefaultTableModel) table.getModel();
					if (e.getClickCount() == 2) {
						int selectedRow = table.getSelectedRow();
						if (selectedRow != -1) {
							int productId = Integer.valueOf((String) (dtm.getValueAt(selectedRow, 0)));
							int productQuantity = Integer.valueOf((String) (dtm.getValueAt(selectedRow, 4)));

							int buyQuantity = Integer.valueOf(JOptionPane.showInputDialog("Nhập số lượng: "));
							if (buyQuantity > productQuantity) {
								JOptionPane.showMessageDialog(ProductManagementView.this, "Không đủ số lượng", "Error",
										JOptionPane.ERROR_MESSAGE);
								return;
							}

							Product selectedProduct;
							try {
								selectedProduct = productController.getProductById(productId);
								ps.onProductSelected(selectedProduct, buyQuantity);
								dispose();
							} catch (ClassNotFoundException | IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			});
		}

		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void updateProductTable(List<Product> products) {
		DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		dtm.setRowCount(0);
		products.forEach(product -> {
			try {
				dtm.addRow(new Object[] { String.valueOf(product.getId()), product.getName(),
						categoryController.getCategoryById(product.getCategoryId()).getName(),
						String.valueOf(product.getPrice()), String.valueOf(product.getQuantity()) });
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		});
	}

	public Product getSelectedProduct() {
		DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		int row = table.getSelectedRow();
		if (row == -1)
			return null;
		int id = Integer.valueOf(String.valueOf(dtm.getValueAt(row, 0)));
		try {
			Product product = productController.getProductById(id);
			return product;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Product> getDataFromTable() {
		List<Product> products = new ArrayList<>();
		DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
		int rowCount = dtm.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			int id = Integer.parseInt(dtm.getValueAt(i, 0).toString());
			try {
				Product product = productController.getProductById(id);
				products.add(product);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		return products;
	}

}
