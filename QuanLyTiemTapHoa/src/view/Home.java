package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import controller.BillController;
import controller.ProductController;
import dao.BillDAO;
import dao.ProductDAO;
import model.Bill;
import model.User;
import javax.swing.border.LineBorder;

public class Home extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;

	public Home(User user) {
		
		setResizable(false);
		setTitle("Quản lý tiệm tạp hoá Xanh");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 720);
		
		mainPanel = new JPanel() {
			 @Override
	            protected void paintComponent(Graphics g) {
	                super.paintComponent(g);
	                try {
	                    Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/supermarket-4052658_1280.jpg"));
	                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
		};
		
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		mainPanel.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 201, 50);
		mainPanel.add(panel);
		panel.setLayout(null);
		
		String helloText = "Xin chào, " + user.getUsername() + "!";
		JLabel helloLabel = new JLabel(helloText);
		helloLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		helloLabel.setBounds(10, 11, 168, 28);
		panel.add(helloLabel);

		JButton logoutBtn = new JButton("Đăng xuất");
		logoutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(Home.this, "Bạn có muốn đăng xuất không?");
				if (option == 0) {
					dispose();
					new LoginView();
				}
			}
		});
		logoutBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		logoutBtn.setBounds(422, 382, 292, 56);
		mainPanel.add(logoutBtn);

		JButton categoryBtn = new JButton("Quản lý loại sản phẩm");
		categoryBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new CategoryManagementView(user);
			}
		});
		categoryBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		categoryBtn.setBounds(107, 250, 292, 56);
		mainPanel.add(categoryBtn);

		JButton productBtn = new JButton("Quản lý sản phẩm");
		productBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new ProductManagementView(user);
			}
		});
		productBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		productBtn.setBounds(107, 316, 292, 56);
		mainPanel.add(productBtn);

		JButton billBtn = new JButton("Quản lý hóa đơn");
		billBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new BillManagementView(user);
			}
		});
		billBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		billBtn.setBounds(107, 382, 292, 56);
		mainPanel.add(billBtn);

		JButton verifyUserBtn = new JButton("Xác minh tài khoản");
		verifyUserBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VerifyUserView(user);
			}
		});
		verifyUserBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		verifyUserBtn.setBounds(422, 250, 292, 56);
		mainPanel.add(verifyUserBtn);

		JButton statBtn = new JButton("Thống kê");
		statBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				Map<String, Double> revenue = new TreeMap<String, Double>();

				BillDAO billDAO = new BillDAO();
				BillController billController = new BillController(billDAO, null);
				try {
					List<Bill> bills = billController.getAllBills();

					for (int i = 1; i <= 12; i++) {
						String month = String.format("%02d", i);
						revenue.put(month, revenue.getOrDefault(month, 0.0));
					}

					for (Bill bill : bills) {
						String month = bill.getDate().substring(3, 5);
						revenue.put(month, revenue.getOrDefault(month, 0.0) + bill.getTotal());
					}

					for (Map.Entry<String, Double> entry : revenue.entrySet()) {
						dataset.addValue(entry.getValue(), "Doanh thu (đồng)", entry.getKey());
					}

					JFreeChart barChart = ChartFactory.createBarChart("Thống kê doanh thu", "Tháng", "Doanh thu (đồng)",
							dataset, PlotOrientation.VERTICAL, true, true, false);

					JFrame frame = new JFrame();
					frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					frame.setSize(960, 540);
					frame.getContentPane().add(new ChartPanel(barChart));
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		statBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		statBtn.setBounds(107, 515, 292, 56);
		mainPanel.add(statBtn);

		JButton changePassBtn = new JButton("Đổi mật khẩu");
		changePassBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChangePasswordView(user);
			}
		});
		
		changePassBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		changePassBtn.setBounds(422, 316, 292, 56);
		mainPanel.add(changePassBtn);

		JPanel productPanel = new JPanel();
		productPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		productPanel.setBounds(930, 496, 250, 101);
		mainPanel.add(productPanel);
		productPanel.setLayout(null);

		JLabel productTitle = new JLabel("Sản phẩm tồn kho", SwingConstants.CENTER);
		productTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		productTitle.setBounds(53, 10, 139, 21);
		productPanel.add(productTitle);

		ProductDAO productDAO = new ProductDAO();
		ProductController productController = new ProductController(productDAO, null);
		try {
			int productNums = productController.getAllProducts().size();
			JLabel productNum = new JLabel(String.valueOf(productNums), SwingConstants.CENTER);
			productNum.setFont(new Font("Tahoma", Font.PLAIN, 40));
			productNum.setBounds(53, 41, 139, 47);
			productPanel.add(productNum);
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
		
		JPanel billPanel = new JPanel();
		billPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		billPanel.setLayout(null);
		billPanel.setBounds(930, 365, 250, 101);
		mainPanel.add(billPanel);

		JLabel billTitle = new JLabel("Hoá đơn đã tạo", SwingConstants.CENTER);
		billTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		billTitle.setBounds(57, 10, 139, 21);
		billPanel.add(billTitle);

		BillDAO billDAO = new BillDAO();
		BillController billController = new BillController(billDAO, null);
		try {
			int billNums = billController.getAllBills().size();
			JLabel billNum = new JLabel(String.valueOf(billNums), SwingConstants.CENTER);
			billNum.setFont(new Font("Tahoma", Font.PLAIN, 40));
			billNum.setBounds(57, 44, 139, 47);
			billPanel.add(billNum);
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}

		JPanel revenuePanel = new JPanel();
		revenuePanel.setLayout(null);
		revenuePanel.setBounds(792, 234, 388, 101);
		mainPanel.add(revenuePanel);

		ImageIcon revenueIcon = new ImageIcon(getClass().getResource(""));
		Image revenueImg = revenueIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		revenueIcon = new ImageIcon(revenueImg);

		JLabel revenueTitle = new JLabel("Doanh thu tháng này", SwingConstants.CENTER);
		revenueTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		revenueTitle.setBounds(88, 10, 189, 21);
		revenuePanel.add(revenueTitle);

		try {
			double revenue = 0;
			List<Bill> bills;
			bills = billController.getAllBills();
			LocalDate today = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
			String todayMonth = today.format(formatter);
			for (Bill bill : bills) {
				String month = bill.getDate().substring(3, 5);
				if (todayMonth.equals(month)) {
					revenue += bill.getTotal();
				}
			}
			revenue /= 1000000;
			String revenueStr = String.format("%.2f", revenue).concat(" triệu đồng");
			JLabel revenueNum = new JLabel(revenueStr, SwingConstants.CENTER);
			revenueNum.setFont(new Font("Tahoma", Font.PLAIN, 40));
			revenueNum.setBounds(36, 41, 293, 47);
			revenuePanel.add(revenueNum);

		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}

		JButton addBillBtn = new JButton("Tạo hóa đơn");
		addBillBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new BillView(billController, user);
			}
		});
		addBillBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		addBillBtn.setBounds(107, 448, 292, 56);
		mainPanel.add(addBillBtn);
		
		JPanel billPanel_1 = new JPanel();
		billPanel_1.setLayout(null);
		billPanel_1.setBounds(181, 89, 829, 111);
		mainPanel.add(billPanel_1);
		
		JLabel titleLableHome = new JLabel("Phần mềm quản lý tiệm tạp hoá Xanh", SwingConstants.CENTER);
		titleLableHome.setFont(new Font("Tahoma", Font.PLAIN, 40));
		titleLableHome.setBounds(10, 25, 804, 64);
		billPanel_1.add(titleLableHome);

		setLocationRelativeTo(null);
		setVisible(true);
	}
}
