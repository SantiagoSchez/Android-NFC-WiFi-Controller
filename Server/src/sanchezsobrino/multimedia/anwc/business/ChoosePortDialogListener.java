package sanchezsobrino.multimedia.anwc.business;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

public class ChoosePortDialogListener implements ActionListener {
	private Window window;
	private JSpinner portSpinner;
	private JLabel status;
	
	public ChoosePortDialogListener(Window window, JSpinner portSpinner, JLabel status) {
		super();
		
		this.window = window;
		this.portSpinner = portSpinner;
		this.status = status;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			int server_port = (int) portSpinner.getValue();
			ServerSocket socket = new ServerSocket(server_port);
			socket.close();
			status.setText("1");
			
			window.dispose();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(window,
					new LocalizedString("port_busy").toString(),
				    new LocalizedString("error_word").toString(),
				    JOptionPane.ERROR_MESSAGE);
		}
	}
}