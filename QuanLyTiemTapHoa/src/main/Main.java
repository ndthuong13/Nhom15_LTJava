package main;

import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import view.LoginView;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> {
			new LoginView();
		});
	}
}
//Chương trình còn lỗi: 
//Tại phần xem chi tiết ở quản lý loại sản phẩm chưa hiển thị nội dung
//Khi vào phần quản lý sản phẩm, vẫn còn bug input == null
//Lỗi phẩn quản lý sản phẩm sảy ra tương tự đối với tạo hoá đơn