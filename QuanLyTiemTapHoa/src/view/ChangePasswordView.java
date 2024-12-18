package view;

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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dao.UserDAO;
import model.User;
import util.FormUtils;
import util.HashPassword;

public class ChangePasswordView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTextField curPassField;
	private JTextField rePassField;
	private JTextField newPassField;
	private UserDAO userDAO;
	private boolean isCurPassHide;
	private boolean isNewPassHide;
	private boolean isRePassHide;

	public ChangePasswordView(User user) {
		userDAO = new UserDAO();
		setResizable(false);
		setTitle("Đổi mật khẩu");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(mainPanel);
		mainPanel.setLayout(null);

		JLabel titleLabel = new JLabel("ĐỔI MẬT KHẨU", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		titleLabel.setBounds(78, 10, 434, 42);
		mainPanel.add(titleLabel);

		JButton changePassBtn = new JButton("ĐỔI MẬT KHẨU");
		changePassBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!FormUtils.ValidateForm(mainPanel)) {
					JOptionPane.showMessageDialog(ChangePasswordView.this, "Vui lòng nhập đầy đủ thông tin", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				String curPass = curPassField.getText();
				String newPass = newPassField.getText();
				String rePass = rePassField.getText();

				if (!user.getPassword().equals(HashPassword.hashPassword(curPass))) {
					JOptionPane.showMessageDialog(ChangePasswordView.this, "Mật khẩu sai. Vui lòng thử lại.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (newPass.equals(curPass)) {
					JOptionPane.showMessageDialog(ChangePasswordView.this, "Mật khẩu mới không được trùng mật khẩu cũ.",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!newPass.equals(rePass)) {
					JOptionPane.showMessageDialog(ChangePasswordView.this, "Mật khẩu không khớp. Vui lòng nhập lại.",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				user.setPassword(HashPassword.hashPassword(newPass));
				try {
					userDAO.update(user);
					JOptionPane.showMessageDialog(ChangePasswordView.this, "Thay đổi mật khẩu thành công.");
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}

				dispose();
			}
		});
		changePassBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		changePassBtn.setBounds(215, 284, 137, 38);
		mainPanel.add(changePassBtn);

		isCurPassHide = true;
		isNewPassHide = true;
		isRePassHide = true;

		JButton toggleCurPassBtn = new JButton("Hiện");
		toggleCurPassBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		toggleCurPassBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isCurPassHide) {
					((JPasswordField) curPassField).setEchoChar((char) 0);

				} else {
					((JPasswordField) curPassField).setEchoChar('●');

				}
				isCurPassHide = !isCurPassHide;
			}
		});
		toggleCurPassBtn.setBounds(371, 82, 76, 42);
		toggleCurPassBtn.setOpaque(false);

		mainPanel.add(toggleCurPassBtn);

		JButton toggleNewPassBtn = new JButton("Hiện");
		toggleNewPassBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		toggleNewPassBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isNewPassHide) {
					((JPasswordField) newPassField).setEchoChar((char) 0);

				} else {
					((JPasswordField) newPassField).setEchoChar('●');
				}
				isNewPassHide = !isNewPassHide;
			}
		});
		toggleNewPassBtn.setBounds(371, 159, 79, 42);
		toggleNewPassBtn.setOpaque(false);
		mainPanel.add(toggleNewPassBtn);

		JButton toggleRePassBtn = new JButton("Hiện");
		toggleRePassBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		toggleRePassBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isRePassHide) {
					((JPasswordField) rePassField).setEchoChar((char) 0);

				} else {
					((JPasswordField) rePassField).setEchoChar('●');

				}
				isRePassHide = !isRePassHide;
			}
		});
		toggleRePassBtn.setBounds(371, 232, 79, 42);
		toggleRePassBtn.setOpaque(false);

		mainPanel.add(toggleRePassBtn);

		rePassField = new JPasswordField();
		rePassField.setBounds(163, 232, 204, 42);
		mainPanel.add(rePassField);
		rePassField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rePassField.setColumns(10);
		rePassField.setBorder(BorderFactory.createCompoundBorder(rePassField.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		JLabel rePassLabel = new JLabel("Nhập lại mật khẩu");
		rePassLabel.setBounds(163, 211, 116, 20);
		mainPanel.add(rePassLabel);
		rePassLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

		newPassField = new JPasswordField();
		newPassField.setBounds(163, 159, 204, 42);
		mainPanel.add(newPassField);
		newPassField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		newPassField.setColumns(10);
		newPassField.setBorder(BorderFactory.createCompoundBorder(newPassField.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		JLabel newPassLabel = new JLabel("Mật khẩu mới");
		newPassLabel.setBounds(163, 134, 116, 20);
		mainPanel.add(newPassLabel);
		newPassLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

		curPassField = new JPasswordField();
		curPassField.setBounds(163, 82, 204, 42);
		mainPanel.add(curPassField);
		curPassField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		curPassField.setColumns(10);
		curPassField.setBorder(BorderFactory.createCompoundBorder(curPassField.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		JLabel curPassLabel = new JLabel("Mật khẩu hiện tại");
		curPassLabel.setBounds(163, 62, 116, 20);
		mainPanel.add(curPassLabel);
		curPassLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

		setLocationRelativeTo(null);
		setVisible(true);
	}
}
