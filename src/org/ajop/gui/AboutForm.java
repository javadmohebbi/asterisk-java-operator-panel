package org.ajop.gui;

import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.GridLayout;
import java.net.URL;

import javax.swing.JTextArea;

public class AboutForm {

	public JFrame frmAbout;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AboutForm window = new AboutForm();
					window.frmAbout.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AboutForm() {
		initialize();
		centerilizedForm();
	}

	/*
	 * Set Location to Center
	 */
	private void centerilizedForm() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
		Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
		int x = ((int) rect.getMaxX()/2)  - (frmAbout.getWidth()/2) ;
		int y = ((int) rect.getMaxY()/2) - (frmAbout.getHeight()/2);
		frmAbout.setLocation(x, y);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAbout = new JFrame();
		frmAbout.setResizable(false);
		frmAbout.setTitle("About");
		frmAbout.setBounds(100, 100, 450, 181);
		frmAbout.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmAbout.getContentPane().setLayout(new GridLayout(0, 1, 10, 10));
		
		URL url = SettingForm.class.getResource("/icon.png");
		ImageIcon img = new ImageIcon(url);
		frmAbout.setIconImage(img.getImage());
		
		
		JTextArea txtrAsteriskJavaOperator = new JTextArea();
		txtrAsteriskJavaOperator.setText("Asterisk Java Operator Panel\nVersion: 1.0.0\nLicensed under IDC (I Don't Care) license agreement.\n\nThis project is based on great asterisk-java project\n\nWebsite: http://ir-linux.ir/opensource\nEmail Address: m@ir-linux.ir\nMohammad Javad Mohebbi");
		txtrAsteriskJavaOperator.setEditable(false);
		frmAbout.getContentPane().add(txtrAsteriskJavaOperator);
	}
}
