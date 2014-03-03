package sanchezsobrino.multimedia.anwc.presentation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import sanchezsobrino.multimedia.anwc.business.ChoosePortDialogListener;
import sanchezsobrino.multimedia.anwc.business.LocalizedString;
import sun.swing.DefaultLookup;

public class ChoosePortDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JSpinner choosePortSpinner;
	private JLabel choosePortLabel;
	private JSeparator separator;
	private JLabel iconLabel;
	private JButton button;
	
	private JLabel status;
	
	public ChoosePortDialog(JFrame frame, String title) {
		super(frame, title, true);
		setResizable(false);

		JOptionPane jop = new JOptionPane();
		
		status = new JLabel("0");

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		choosePortLabel = new JLabel(new LocalizedString("choose_port").toString());
		GridBagConstraints gbc_choosePortLabel = new GridBagConstraints();
		gbc_choosePortLabel.anchor = GridBagConstraints.EAST;
		gbc_choosePortLabel.ipady = 5;
		gbc_choosePortLabel.ipadx = 5;
		gbc_choosePortLabel.insets = new Insets(5, 5, 0, 5);
		gbc_choosePortLabel.gridx = 1;
		gbc_choosePortLabel.gridy = 0;
		getContentPane().add(choosePortLabel, gbc_choosePortLabel);

		iconLabel = new JLabel(DefaultLookup.getIcon(jop, jop.getUI(), "OptionPane.warningIcon"));
		GridBagConstraints gbc_iconLabel = new GridBagConstraints();
		gbc_iconLabel.fill = GridBagConstraints.BOTH;
		gbc_iconLabel.insets = new Insets(0, 5, 0, 5);
		gbc_iconLabel.gridx = 0;
		gbc_iconLabel.gridy = 1;
		getContentPane().add(iconLabel, gbc_iconLabel);

		choosePortSpinner = new JSpinner(new SpinnerNumberModel(8888, 1024, 65535, 1));
		GridBagConstraints gbc_choosePortSpinner = new GridBagConstraints();
		gbc_choosePortSpinner.anchor = GridBagConstraints.EAST;
		gbc_choosePortSpinner.ipadx = 5;
		gbc_choosePortSpinner.insets = new Insets(0, 5, 5, 5);
		gbc_choosePortSpinner.gridx = 1;
		gbc_choosePortSpinner.gridy = 1;
		getContentPane().add(choosePortSpinner, gbc_choosePortSpinner);
		choosePortSpinner.setEditor(new JSpinner.NumberEditor(choosePortSpinner, "#"));

		separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.BOTH;
		gbc_separator.insets = new Insets(0, 5, 0, 5);
		gbc_separator.gridx = 1;
		gbc_separator.gridy = 2;
		getContentPane().add(separator, gbc_separator);

		button = new JButton("Aceptar");
		button.addActionListener(new ChoosePortDialogListener(frame, choosePortSpinner, status));
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.ipadx = 10;
		gbc_button.anchor = GridBagConstraints.EAST;
		gbc_button.insets = new Insets(5, 5, 5, 5);
		gbc_button.gridx = 1;
		gbc_button.gridy = 3;
		gbc_button.ipadx = 50;
		getContentPane().add(button, gbc_button);

		this.getRootPane().setDefaultButton(button);

		pack();
		setLocationRelativeTo(frame);
	}
	
	public int getServerPort() {
		return (int) choosePortSpinner.getValue();
	}
	
	public int getStatus() {
		return Integer.parseInt(status.getText());
	}
}
