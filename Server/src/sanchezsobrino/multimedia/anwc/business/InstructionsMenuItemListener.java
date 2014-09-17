package sanchezsobrino.multimedia.anwc.business;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class InstructionsMenuItemListener implements ActionListener {
	private Window window;
	private StringBuffer text;

	public InstructionsMenuItemListener(Window window) {
		super();

		this.window = window;
		this.text = new StringBuffer();
		this.text.append(new LocalizedString("instructions_text1")).append(System.getProperty("line.separator"));
		this.text.append(new LocalizedString("instructions_text2")).append(System.getProperty("line.separator"));
		this.text.append(new LocalizedString("instructions_text3")).append(System.getProperty("line.separator"));
		this.text.append(new LocalizedString("instructions_text4")).append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
		this.text.append(new LocalizedString("instructions_text5")).append(System.getProperty("line.separator"));
		this.text.append(new LocalizedString("instructions_text6")).append(System.getProperty("line.separator"));
		this.text.append(new LocalizedString("instructions_text7")).append(System.getProperty("line.separator"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(window, text, new LocalizedString("instructions_menu_item").toString(), JOptionPane.INFORMATION_MESSAGE);
	}
}
