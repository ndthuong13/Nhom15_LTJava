package view;

import java.awt.BorderLayout;
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

import controller.UserController;
import dao.UserDAO;
import model.User;
import util.FormUtils;
import util.SendMail;
import javax.swing.SwingConstants;
import java.awt.Color;

public class SignupView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTextField usernameField;
	private JTextField passwordField;
	private JTextField rePasswordField;
	private JTextField emailField;
	private JTextField otpField;
	private SendMail sm;
	private UserDAO userDAO;
	private boolean isPassHide;
	private boolean isRePassHide;

	public SignupView() {
		sm = new SendMail();
		userDAO = new UserDAO();
		setResizable(false);
		setTitle("Đăng ký tài khoản");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 660, 500);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(mainPanel);
		mainPanel.setLayout(new BorderLayout(0, 0));

		ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/store_logo.png"));
		Image logoImg = logoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
		logoIcon = new ImageIcon(logoImg);
		
		JPanel signupPanel = new JPanel();
		mainPanel.add(signupPanel, BorderLayout.CENTER);
		signupPanel.setLayout(null);

		JPanel usernamePanel = new JPanel();
		usernamePanel.setBounds(169, 11, 299, 79);
		signupPanel.add(usernamePanel);
		usernamePanel.setLayout(null);

		JLabel usernameLabel = new JLabel("Tên đăng nhập");
		usernameLabel.setBounds(4, 8, 120, 17);
		usernameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		usernamePanel.add(usernameLabel);

		usernameField = new JTextField();
		usernameField.setToolTipText("Hãy nhập tên đăng nhập");
		usernameField.setBounds(0, 32, 299, 36);
		usernameField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		usernameLabel.setLabelFor(usernameField);
		usernamePanel.add(usernameField);
		usernameField.setColumns(15);
		usernameField.setBorder(BorderFactory.createCompoundBorder(usernameField.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		JPanel passwordPanel = new JPanel();
		passwordPanel.setBounds(169, 78, 299, 79);
		signupPanel.add(passwordPanel);
		passwordPanel.setLayout(null);

		JLabel passwordLabel = new JLabel("Mật khẩu");
		passwordLabel.setBounds(4, 8, 105, 17);
		passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		passwordPanel.add(passwordLabel);

		passwordField = new JPasswordField();
		passwordField.setToolTipText("Hãy nhập tên mật khẩu");
		passwordField.setBounds(0, 32, 299, 36);
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordField.setColumns(15);
		passwordField.setBorder(BorderFactory.createCompoundBorder(passwordField.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		passwordPanel.add(passwordField);

		JButton signupBtn = new JButton("ĐĂNG KÝ");
		signupBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!FormUtils.ValidateForm(mainPanel)) {
					JOptionPane.showMessageDialog(SignupView.this, "Vui lòng nhập đầy đủ thông tin", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				String username = usernameField.getText();
				String password = passwordField.getText();
				String rePassword = rePasswordField.getText();
				String email = emailField.getText();
				String otp = otpField.getText();

				if (!password.equals(rePassword)) {
					JOptionPane.showMessageDialog(SignupView.this, "Vui lòng kiểm tra lại mật khẩu", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!otp.equals(sm.getOtp())) {
					JOptionPane.showMessageDialog(SignupView.this, "Vui lòng kiểm tra lại mã xác nhận", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				UserDAO userDAO = new UserDAO();
				UserController userController = new UserController(userDAO);
				int id;
				try {
					id = userController.getAllUsers().size() + 1;
					User user = new User(id, username, email, password, false);
					if (!userDAO.add(user)) {
						JOptionPane.showMessageDialog(SignupView.this,
								"Đã tồn tại tài khoản với tên tài khoản hoặc email này. Vui lòng thử lại.", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}

				JOptionPane.showMessageDialog(SignupView.this, "Đăng ký thành công");

				FormUtils.resetForm(mainPanel);
				dispose();
				new LoginView();
			}
		});
		signupBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		signupBtn.setBounds(169, 413, 148, 32);
		signupPanel.add(signupBtn);

		JPanel rePasswordPanel = new JPanel();
		rePasswordPanel.setLayout(null);
		rePasswordPanel.setBounds(169, 167, 299, 79);
		signupPanel.add(rePasswordPanel);

		JLabel rePasswordLabel = new JLabel("Nhập lại mật khẩu");
		rePasswordLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		rePasswordLabel.setBounds(4, 8, 174, 17);
		rePasswordPanel.add(rePasswordLabel);

		rePasswordField = new JPasswordField();
		rePasswordField.setToolTipText("Hãy nhập lại mật khẩu");
		rePasswordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rePasswordField.setColumns(15);
		rePasswordField.setBorder(BorderFactory.createCompoundBorder(rePasswordField.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		rePasswordField.setBounds(0, 32, 299, 36);
		rePasswordPanel.add(rePasswordField);

		JPanel emailPanel = new JPanel();
		emailPanel.setLayout(null);
		emailPanel.setBounds(169, 245, 299, 79);
		signupPanel.add(emailPanel);

		JLabel emailLabel = new JLabel("Email");
		emailLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		emailLabel.setBounds(4, 8, 130, 17);
		emailPanel.add(emailLabel);

		emailField = new JTextField();
		emailField.setToolTipText("Hãy nhập email");
		emailField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		emailField.setColumns(15);
		emailField.setBorder(BorderFactory.createCompoundBorder(emailField.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		emailField.setBounds(0, 32, 299, 36);
		emailPanel.add(emailField);

		JPanel otpPanel = new JPanel();
		otpPanel.setLayout(null);
		otpPanel.setBounds(169, 323, 299, 79);
		signupPanel.add(otpPanel);

		JLabel otpLabel = new JLabel("Mã xác nhận");
		otpLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		otpLabel.setBounds(4, 8, 130, 17);
		otpPanel.add(otpLabel);

		otpField = new JTextField();
		otpField.setToolTipText("Hãy nhập mã xác nhận");
		otpField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		otpField.setColumns(15);
		otpField.setBorder(BorderFactory.createCompoundBorder(otpField.getBorder(),

				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		otpField.setBounds(0, 32, 299, 36);
		otpPanel.add(otpField);

		JButton otpBtn = new JButton("GỬI MÃ");
		otpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String email = emailField.getText();

				if (email.isEmpty()) {
					JOptionPane.showMessageDialog(SignupView.this, "Vui lòng điền email.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					if (userDAO.isUserExist(username, email)) {
						JOptionPane.showMessageDialog(SignupView.this,
								"Đã tồn tại tài khoản với tên tài khoản hoặc email này. Vui lòng thử lại.", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				if (!sm.sendOtp(emailField.getText())) {
					JOptionPane.showMessageDialog(SignupView.this,
							"Có lỗi trong quá trình lấy mã xác nhận. Vui lòng thử lại", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(SignupView.this, "Mã xác nhận đã được gửi vào email. Vui lòng kiểm tra.");
			}
		});
		otpBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		otpBtn.setBounds(478, 354, 148, 37);
		signupPanel.add(otpBtn);

		isPassHide = true;
		isRePassHide = true;

		JButton togglePassBtn = new JButton("Hiện mật khẩu");
		togglePassBtn.setHorizontalAlignment(SwingConstants.LEFT);
		togglePassBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isPassHide) {
					((JPasswordField) passwordField).setEchoChar((char) 0);
					
				} else {
					((JPasswordField) passwordField).setEchoChar('●');
				}
				isPassHide = !isPassHide;
			}
		});
		togglePassBtn.setOpaque(false);
		togglePassBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		togglePassBtn.setBounds(485, 119, 141, 36);
		signupPanel.add(togglePassBtn);

		JButton toggleRePassBtn = new JButton("Hiện mật khẩu");
		toggleRePassBtn.setHorizontalAlignment(SwingConstants.LEFT);
		toggleRePassBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isRePassHide) {
					((JPasswordField) rePasswordField).setEchoChar((char) 0);
				} else {
					((JPasswordField) rePasswordField).setEchoChar('●');
				}
				isRePassHide = !isRePassHide;
			}
		});
		toggleRePassBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		toggleRePassBtn.setBounds(485, 199, 141, 36);
		toggleRePassBtn.setOpaque(false);
		signupPanel.add(toggleRePassBtn);
		
				JButton loginBtn = new JButton("ĐĂNG NHẬP");
				loginBtn.setBounds(319, 413, 149, 32);
				signupPanel.add(loginBtn);
				loginBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						FormUtils.resetForm(mainPanel);
						dispose();
						new LoginView();
					}
				});
				loginBtn.setFont(new Font("Tahoma", Font.BOLD, 14));

		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
