package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import dao.UserDAO;
import model.User;
import util.FormUtils;
import util.HashPassword;
import util.PasswordGenerator;
import util.SendMail;

public class ResetPasswordView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTextField emailField;
	private JTextField otpField;
	private SendMail sm;
	private UserDAO userDAO;

	/**
	 * Create the frame.
	 */
	public ResetPasswordView() {
		sm = new SendMail();
		userDAO = new UserDAO();
		setTitle("Lấy lại mật khẩu");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

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

		JPanel emailPanel = new JPanel();
		emailPanel.setBounds(134, 10, 299, 79);
		loginPanel.add(emailPanel);
		emailPanel.setLayout(null);

		JLabel emailLabel = new JLabel("Email");
		emailLabel.setBounds(4, 8, 106, 17);
		emailLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		emailPanel.add(emailLabel);

		emailField = new JTextField();
		emailField.setToolTipText("Hãy nhập email");
		emailField.setBounds(0, 32, 299, 36);
		emailField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		emailLabel.setLabelFor(emailField);
		emailPanel.add(emailField);
		emailField.setColumns(15);
		emailField.setBorder(BorderFactory.createCompoundBorder(emailField.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		JPanel otpPanel = new JPanel();
		otpPanel.setBounds(134, 87, 299, 79);
		loginPanel.add(otpPanel);
		otpPanel.setLayout(null);

		JLabel otpLabel = new JLabel("Mã xác nhận");
		otpLabel.setBounds(4, 8, 93, 17);
		otpLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		otpPanel.add(otpLabel);

		otpField = new JTextField();
		otpField.setToolTipText("Hãy nhập mã xác nhận");
		otpField.setBounds(0, 32, 299, 36);
		otpField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		otpField.setColumns(15);
		otpField.setBorder(
				BorderFactory.createCompoundBorder(otpField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		otpPanel.add(otpField);

		JButton resetPassBtn = new JButton("CẤP LẠI MẬT KHẨU");
		resetPassBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = emailField.getText();
				String otp = otpField.getText();

				if (!FormUtils.ValidateForm(mainPanel)) {
					JOptionPane.showMessageDialog(ResetPasswordView.this, "Vui lòng nhập đầy đủ thông tin", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					User user = userDAO.get(u -> u.getEmail().equals(email));
					if (user == null) {
						JOptionPane.showMessageDialog(ResetPasswordView.this, "Email không tồn tại. Vui lòng thử lại.",
								"Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				if (!otp.equals(sm.getOtp())) {
					JOptionPane.showMessageDialog(ResetPasswordView.this, "Vui lòng kiểm tra lại mã xác nhận", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				String password = PasswordGenerator.generatePassword();

				if (!sm.sendNewPassword(email, password)) {
					JOptionPane.showMessageDialog(ResetPasswordView.this,
							"Có lỗi trong quá trình lấy lại mật khẩu. Vui lòng thử lại", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					User user = userDAO.get(u -> u.getEmail().equals(email));
					user.setPassword(HashPassword.hashPassword(password));
					if (!userDAO.update(user)) {
						JOptionPane.showMessageDialog(ResetPasswordView.this,
								"Có lỗi trong quá trình lấy lại mật khẩu. Vui lòng thử lại", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				JOptionPane.showMessageDialog(ResetPasswordView.this,
						"Mật khẩu mới đã được gửi về email. Vui lòng kiểm tra");

				FormUtils.resetForm(mainPanel);
				dispose();
				new LoginView();
			}
		});
		resetPassBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		resetPassBtn.setBounds(134, 176, 299, 32);
		loginPanel.add(resetPassBtn);

		JButton otpBtn = new JButton("GỬI MÃ");
		otpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = emailField.getText();

				if (email.isEmpty()) {
					JOptionPane.showMessageDialog(ResetPasswordView.this, "Vui lòng điền email.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!sm.sendOtp(emailField.getText())) {
					JOptionPane.showMessageDialog(ResetPasswordView.this,
							"Có lỗi trong quá trình lấy mã xác nhận. Vui lòng thử lại", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(ResetPasswordView.this,
						"Mã xác nhận đã được gửi vào email. Vui lòng kiểm tra.");
			}
		});
		otpBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		otpBtn.setBounds(436, 118, 92, 37);
		loginPanel.add(otpBtn);

		JButton loginBtn = new JButton("QUAY LẠI ĐĂNG NHẬP");
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new LoginView();
			}
		});
		loginBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		loginBtn.setBounds(134, 218, 299, 32);
		loginPanel.add(loginBtn);

		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
