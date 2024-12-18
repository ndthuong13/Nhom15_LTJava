package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import dao.UserDAO;
import model.User;
import util.FormUtils;
import util.HashPassword;
import javax.swing.SwingConstants;

public class LoginView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTextField usernameField;
	private JTextField passwordField;
	private UserDAO userDAO;
	boolean isHide;

	/**
	 * Create the frame.
	 */
	public LoginView() {
		setResizable(false);
		setBackground(new Color(255, 255, 255));
		userDAO = new UserDAO();
		setTitle("Quản lý tiệm tạp hoá xanh");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 450);
		mainPanel = new JPanel();

		setContentPane(mainPanel);
		mainPanel.setLayout(new BorderLayout(0, 0));

		ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/store_logo.png"));
		Image logoImg = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		logoIcon = new ImageIcon(logoImg);
		JLabel logoLabel = new JLabel(logoIcon);
		JPanel logoPanel = new JPanel();
		logoPanel.add(logoLabel);

		mainPanel.add(logoPanel, BorderLayout.NORTH);

		JPanel loginPanel = new JPanel();
		mainPanel.add(loginPanel, BorderLayout.CENTER);
		loginPanel.setLayout(null);

		JPanel usernamePanel = new JPanel();
		usernamePanel.setBounds(135, 10, 299, 79);
		loginPanel.add(usernamePanel);
		usernamePanel.setLayout(null);

		JLabel usernameLabel = new JLabel("Tên đăng nhập");
		usernameLabel.setBounds(4, 8, 164, 17);
		usernameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		usernamePanel.add(usernameLabel);

		usernameField = new JTextField();
		usernameField.setBounds(0, 33, 299, 36);
		usernameField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		usernameLabel.setLabelFor(usernameField);
		usernamePanel.add(usernameField);
		usernameField.setColumns(15);
		usernameField.setBorder(BorderFactory.createCompoundBorder(usernameField.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		JPanel passwordPanel = new JPanel();
		passwordPanel.setBounds(135, 88, 299, 79);
		loginPanel.add(passwordPanel);
		passwordPanel.setLayout(null);

		JLabel passwordLabel = new JLabel("Mật khẩu");
		passwordLabel.setBounds(4, 8, 112, 17);
		passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		passwordPanel.add(passwordLabel);

		passwordField = new JPasswordField();
		passwordField.setBounds(0, 33, 299, 36);
		passwordPanel.add(passwordField);
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordField.setColumns(15);
		passwordField.setBorder(BorderFactory.createCompoundBorder(passwordField.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		JPanel panel = new JPanel();
		panel.setBounds(135, 177, 299, 79);
		loginPanel.add(panel);
		panel.setLayout(null);

		JButton forgotPasswordBtn = new JButton("LẤY LẠI MẬT KHẨU");
		forgotPasswordBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FormUtils.resetForm(mainPanel);
				dispose();
				new ResetPasswordView();
			}
		});
		forgotPasswordBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		forgotPasswordBtn.setBounds(0, 43, 299, 32);

		panel.add(forgotPasswordBtn);

		JButton signupBtn = new JButton("ĐĂNG KÝ");
		signupBtn.setBounds(168, 0, 131, 32);
		panel.add(signupBtn);
		signupBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FormUtils.resetForm(mainPanel);
				dispose();
				new SignupView();
			}
		});
		signupBtn.setFont(new Font("Tahoma", Font.BOLD, 14));

		JButton loginBtn = new JButton("ĐĂNG NHẬP");
		loginBtn.setBounds(0, 1, 131, 32);
		panel.add(loginBtn);
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!FormUtils.ValidateForm(mainPanel)) {
					JOptionPane.showMessageDialog(LoginView.this, "Vui lòng nhập đầy đủ thông tin", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				String username = usernameField.getText();
				String password = passwordField.getText();

				try {
					User user = userDAO.get(u -> u.getUsername().equals(username));

					if (user == null || !user.getPassword().equals(HashPassword.hashPassword(password))) {
						JOptionPane.showMessageDialog(LoginView.this, "Tên đăng nhập hoặc mật khẩu không chính xác.",
								"Error", JOptionPane.ERROR_MESSAGE);
						return;
					}

//					if (!user.isVerify()) {
//						JOptionPane.showMessageDialog(LoginView.this,
//								"Tài khoản của bạn không có quyền truy cập. Vui lòng liên hệ Admin.", "Error",
//								JOptionPane.ERROR_MESSAGE);
//						return;
//					}

					JOptionPane.showMessageDialog(LoginView.this, "Đăng nhập thành công");
					dispose();
					new Home(user);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		loginBtn.setFont(new Font("Tahoma", Font.BOLD, 14));

		isHide = true;
		JButton togglePassBtn = new JButton("Hiện mật khẩu");
		togglePassBtn.setBackground(new Color(255, 255, 255));
		togglePassBtn.setHorizontalAlignment(SwingConstants.LEFT);
		togglePassBtn.setBounds(434, 120, 142, 36);
		loginPanel.add(togglePassBtn);
		togglePassBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isHide) {
					((JPasswordField) passwordField).setEchoChar((char) 0);
				} else {
					((JPasswordField) passwordField).setEchoChar('●');
				}
				isHide = !isHide;
			}
		});
		togglePassBtn.setOpaque(false);
		togglePassBtn.setFont(new Font("Tahoma", Font.BOLD, 14));

		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
