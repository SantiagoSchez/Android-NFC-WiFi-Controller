package sanchezsobrino.multimedia.anwc.presentation;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class LogPane extends JTextPane {
	private static final long serialVersionUID = 1L;
	
	private final Color green = new Color(0x00008800);
	
	public void log(String s) {
		append(Color.black, s);
	}
	
	public void logSuccess(String s) {
		append(green, s);
	}
	
	public void logInfo(String s) {
		append(Color.blue, s);
	}
	
	public void logError(String s) {
		append(Color.red, s);
	}
	
	public void append(Color c, String s) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

		int len = getDocument().getLength();
		setCaretPosition(len);
		setCharacterAttributes(aset, false);

		setEditable(true);
		replaceSelection(s);
		setEditable(false);
	}
}