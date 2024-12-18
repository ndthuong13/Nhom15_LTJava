package util;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class FormUtils {
	public static boolean ValidateForm(JPanel inputPanel) {
		List<JTextField> jtfs = new ArrayList<>();

		collectTextFields(inputPanel, jtfs);

		return jtfs.stream().noneMatch(jtf -> jtf.getText().isEmpty());
	}

	public static void resetForm(JPanel inputPanel) {
		List<JTextField> jtfs = new ArrayList<>();

		collectTextFields(inputPanel, jtfs);

		jtfs.forEach(jtf -> jtf.setText(""));
	}

	private static void collectTextFields(JPanel panel, List<JTextField> jtfs) {
		Component[] components = panel.getComponents();
		for (Component component : components) {
			if (component instanceof JTextField) {
				jtfs.add((JTextField) component);
			} else if (component instanceof JPanel) {
				collectTextFields((JPanel) component, jtfs);
			}
		}
	}
}
