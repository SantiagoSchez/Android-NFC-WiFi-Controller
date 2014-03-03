package sanchezsobrino.multimedia.anwc.business;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class AboutMenuItemListener implements ActionListener {
	private Window window;
	private StringBuffer text;

	public AboutMenuItemListener(Window window) {
		super();

		this.window = window;
		this.text = new StringBuffer();
		this.text.append(new LocalizedString("about_text1")).append(System.lineSeparator());
		this.text.append(new LocalizedString("about_text2")).append(System.lineSeparator()).append(System.lineSeparator());
		this.text.append(new LocalizedString("about_text3")).append(System.lineSeparator());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(window, text, "Android NFC-WiFi Controller [Server]", JOptionPane.INFORMATION_MESSAGE);
	}
}
