package sanchezsobrino.multimedia.anwc.presentation;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultCaret;

import sanchezsobrino.multimedia.anwc.business.AboutMenuItemListener;
import sanchezsobrino.multimedia.anwc.business.ExitMenuItemListener;
import sanchezsobrino.multimedia.anwc.business.InstructionsMenuItemListener;
import sanchezsobrino.multimedia.anwc.business.LocalizedString;
import sanchezsobrino.multimedia.anwc.business.TCPServer;

public class MainWindow {
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setLocationRelativeTo(null);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 550, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Android NFC-WiFi Controller [Server]");

		Image icon = new ImageIcon(getClass().getResource("icon.png")).getImage();
		frame.setIconImage(icon);
		
		/** UI Components **/
		/* Top menu bar */
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		/* File menu */
		JMenu fileMenu = new JMenu(new LocalizedString("file_menu").toString());
		menuBar.add(fileMenu);

		/* Exit menu item */
		JMenuItem exitMenuItem = new JMenuItem(new LocalizedString("exit_menu_item").toString());
		exitMenuItem.addActionListener(new ExitMenuItemListener());
		fileMenu.add(exitMenuItem);

		/* Help menu */
		JMenu helpMenu = new JMenu(new LocalizedString("help_menu").toString());
		menuBar.add(helpMenu);

		/* Instructions menu item */
		JMenuItem instructionsMenuItem = new JMenuItem(new LocalizedString("instructions_menu_item").toString());
		instructionsMenuItem.addActionListener(new InstructionsMenuItemListener(frame));
		instructionsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		helpMenu.add(instructionsMenuItem);

		/* About menu item */
		JMenuItem aboutMenuItem = new JMenuItem(new LocalizedString("about_menu_item").toString() + " ANWC");
		aboutMenuItem.addActionListener(new AboutMenuItemListener(frame));
		helpMenu.add(aboutMenuItem);

		/* Main container */
		JPanel mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

		/* QR Code */
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[] { 550, 0 };
		gbl_mainPanel.rowHeights = new int[] { 250, -243, 0 };
		gbl_mainPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_mainPanel.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		mainPanel.setLayout(gbl_mainPanel);
		JLabel qrLabel = new JLabel();
		qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_qrLabel = new GridBagConstraints();
		gbc_qrLabel.insets = new Insets(0, 0, 5, 0);
		gbc_qrLabel.anchor = GridBagConstraints.NORTH;
		gbc_qrLabel.gridx = 0;
		gbc_qrLabel.gridy = 0;
		mainPanel.add(qrLabel, gbc_qrLabel);

		/* Log pane */
		LogPane logPane = new LogPane();
		logPane.setBackground(SystemColor.windowBorder);
		logPane.setMargin(new Insets(2, 5, 5, 5));
		DefaultCaret caret = (DefaultCaret) logPane.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(logPane);
		logPane.setEditable(false);
		GridBagConstraints gbc_logTextArea = new GridBagConstraints();
		gbc_logTextArea.fill = GridBagConstraints.BOTH;
		gbc_logTextArea.gridx = 0;
		gbc_logTextArea.gridy = 1;
		mainPanel.add(scrollPane, gbc_logTextArea);

		/* Dialog for choosing a port */
		ChoosePortDialog cpd = new ChoosePortDialog(frame, new LocalizedString("attention").toString());
		cpd.setVisible(true);

		if (cpd.getStatus() == 1) {
			TCPServer server_thread = new TCPServer((int) cpd.getServerPort(), qrLabel, logPane, frame);
			server_thread.start();
		} else {
			System.exit(0);
		}
	}
}
