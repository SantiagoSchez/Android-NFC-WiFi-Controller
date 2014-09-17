package sanchezsobrino.multimedia.anwc.business;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import sanchezsobrino.multimedia.anwc.presentation.LogPane;

public class TCPServer extends Thread {
	private ServerSocket server_socket;
	private boolean waiting_for_clients;
	private boolean waiting_for_commands;
	private int port;
	private JLabel qrLabel;
	private LogPane log;
	private JFrame frame;

	String server_started;
	String error_word;
	String critical_error;
	String waiting_for_client;
	String client_connected;
	String client_disconnected;
	String accept_failed;
	String event_received;

	public TCPServer(int port, JLabel qrLabel, LogPane logPane, JFrame frame) {
		this.waiting_for_clients = true;
		this.waiting_for_commands = true;
		this.port = port;
		this.qrLabel = qrLabel;
		this.log = logPane;
		this.frame = frame;
		
		this.server_started = new LocalizedString("server_started").toString();
		this.error_word = new LocalizedString("error_word").toString();
		this.critical_error = new LocalizedString("critical_error").toString();
		this.waiting_for_client = new LocalizedString("waiting_for_client").toString();
		this.client_connected = new LocalizedString("client_connected").toString();
		this.client_disconnected = new LocalizedString("client_disconnected").toString();
		this.accept_failed = new LocalizedString("accept_failed").toString();
		this.event_received = new LocalizedString("event_received").toString();
	}

	@Override
	public void run() {
		server_socket = null;
		String socket_data = getLocalIpAddress() + ":" + port;

		ImageIcon red_qr_code = null;
		ImageIcon green_qr_code = null;

		try {
			server_socket = new ServerSocket(port);
			log.logSuccess(server_started + " " + socket_data + System.getProperty("line.separator"));

			CustomQRCode qr = new CustomQRCode(socket_data, 250, 250);
			red_qr_code = qr.getRedQRCode();
			green_qr_code = qr.getGreenQRCode();
		} catch (IOException e) {
			log.logError(critical_error + ": " + port + System.getProperty("line.separator"));
			JOptionPane.showMessageDialog(log, critical_error, error_word, JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		Socket client_socket = null;
		while (waiting_for_clients) {
			try {
				log.logSuccess(waiting_for_client + System.getProperty("line.separator"));
				qrLabel.setIcon(red_qr_code);

				client_socket = server_socket.accept();
				log.logSuccess(client_connected + " " + client_socket.getInetAddress().getHostAddress() + System.getProperty("line.separator"));
				qrLabel.setIcon(green_qr_code);

				Scanner in = new Scanner(client_socket.getInputStream());
				
				if (waiting_for_commands){
					frame.setExtendedState(Cursor.CROSSHAIR_CURSOR);
				}
				
				while (waiting_for_commands) {
					CodeEvent event = CodeEvent.fromInt(in.nextInt());

					try {
						if (proccessInput(event)) {
							log.logInfo(event_received + ": '" + event + "'" + System.getProperty("line.separator"));
						} else {
							waiting_for_commands = false;
							
						}
					} catch (AWTException e) {
						e.printStackTrace();
					}
				}
				in.close();
			} catch (Exception e) {
				log.logError(accept_failed + System.getProperty("line.separator"));
			} finally {
				try {
					frame.setExtendedState(Cursor.DEFAULT_CURSOR);
					client_socket.close();
				} catch (IOException e) {
					// ...
				}
				qrLabel.setIcon(red_qr_code);
				waiting_for_commands = true;
				log.logError(client_disconnected + " " + client_socket.getInetAddress().getHostAddress() + System.getProperty("line.separator"));
			}
		}

		try {
			server_socket.close();
		} catch (IOException e) {
			// ...
		}
	}

	public boolean proccessInput(CodeEvent event) throws AWTException {
		KeySimulator ks = new KeySimulator();
		boolean result = true;

		switch (event) {
		case ARRIBA:
			ks.simulateKeyPress(KeyEvent.VK_UP);
			break;
		case ABAJO:
			ks.simulateKeyPress(KeyEvent.VK_DOWN);
			break;
		case IZQUIERDA:
			ks.simulateKeyPress(KeyEvent.VK_LEFT);
			break;
		case DERECHA:
			ks.simulateKeyPress(KeyEvent.VK_RIGHT);
			break;
		case START_PRESENTATION:
			ks.simulateKeyPress(KeyEvent.VK_F5);
			break;
		case STOP_PRESENTATION:
			ks.simulateKeyPress(KeyEvent.VK_ESCAPE);
			break;
		case QUIT:
			result = false;
			break;
		default:
			break;
		}

		return result;
	}

	public String getLocalIpAddress() {
		try {
			List<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface ni : nilist) {
				List<InetAddress> ialist = Collections.list(ni.getInetAddresses());
				for (InetAddress address : ialist) {
					if ((!address.isLoopbackAddress()) && (address instanceof Inet4Address)) {
						return address.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
			// ...
		}

		return null;
	}
}