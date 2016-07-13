package org.ajop.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import org.ajop.manage.AsteriskAJOPExceptions;
import org.ajop.manage.AsteriskConfiguration;
import org.ajop.manage.AsteriskCore;
import org.ajop.manage.AsteriskManager;
import org.ajop.manage.Dahdi;
import org.ajop.manage.Sip;
import org.ajop.utils.StringUtility;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueueEntry;
import org.asteriskjava.live.AsteriskServerListener;
import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.live.internal.AsteriskAgentImpl;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.event.ManagerEvent;

public class SettingForm implements AsteriskServerListener, PropertyChangeListener, ManagerEventListener{

	private static JFrame frmSetting;
	private static AsteriskManager asteriskManager;
	private static Dahdi dahdi = null;
	private static Sip sip = null;
	private static AsteriskCore core = null; 
	
	private static Timer timerHandle = null;
	
	private static JPanel rightPanel = null;
	private static JPanel leftPanel = null;
	
	private static SettingForm window = new SettingForm();
	
	private static Map<String, JLabel> extLabelMap = null;
	private static Map<String, JLabel> extImageMap = null;
	private static Map<String, JLabel> extCallLabelMap = null;
	
	private static Map<String, JLabel> chnLabelMap = null;
	private static Map<String, JLabel> chnImageMap = null;
	private static Map<String, JLabel> chnCallLabelMap = null;
	
	private static Color INUSE_COLOR = new Color(255, 0, 0);
	private static Color DEF_COLOR = new Color(30, 30, 30);
	private static Color ACTIVE_COLOR = new Color(222, 222, 222);
	private static Color DEACTIVE_COLOR = new Color(152, 152, 152);
	//private static URL BUSY_OUT = JFrame.class.getResource("/out.png");
	//private static URL BUSY_IN = JFrame.class.getResource("/in.png");
	private static URL EXTENSION = JFrame.class.getResource("/ext.png");
	private static URL CHANNEL = JFrame.class.getResource("/chn.png");
	private static URL EXTENSION_DISABLE = JFrame.class.getResource("/dis-1.png");
	
	
	private static Map<String, String> sipResultLists = new HashMap<String, String>(); 
	private static Map<String, String> channelResultLists = new HashMap<String, String>();
	
	//private static Map<String, String[]> callsHashMap = new HashMap<String, String[]>();
	
	private static StringUtility utils = new StringUtility();
	private static boolean isFirstTime = true;
	
	private static int failedCounter = 1;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try {
					SettingForm win = new SettingForm();
					window = win;
					initContents();
					
					window.frmSetting.setVisible(true);
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AsteriskAJOPExceptions e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AuthenticationFailedException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	private static void ShowErrorMessage() throws AsteriskAJOPExceptions, IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException {
		AsteriskConfiguration config = new AsteriskConfiguration();
		config.readConfigFiles();
		JOptionPane.showMessageDialog(null, "Can not connect to Asterisk Server " +
			config.getIp() + ":" + config.getPort() +
			" using '" + config.getManager() + "' username. \n"
		);
		
		asteriskManager = null;
		initializeConfigForm();//Open Config Form
		
		if (failedCounter >= 3) {
			JOptionPane.showMessageDialog(null, "Program will be terminated in some moments");
			System.exit(0);
		}
		failedCounter++;
		
		initContents(); //Initialize Everything from the begining
	}
	
	/*
	 * Create Content of the Frame
	 */
	private static void initContents() throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AsteriskAJOPExceptions, AuthenticationFailedException{
		
		rightPanel.removeAll();
		leftPanel.removeAll();
		
		if (asteriskManager == null){
			try {
				asteriskManager = new AsteriskManager(window);
			} catch (TimeoutException e){
				e.printStackTrace();
				ShowErrorMessage();
				return;
			} catch (AuthenticationFailedException e){
				e.printStackTrace();
				ShowErrorMessage();
				return;
			} catch (IllegalStateException e) {
				e.printStackTrace();
				ShowErrorMessage();
				return;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				ShowErrorMessage();
				return;
			}
			
		}
		
		List<String[]> dahdiList = null;
		List<String[]> sipList = null;
		dahdi = new Dahdi(asteriskManager.getManagerConnection());
		dahdiList = dahdi.getCommandDahdiShowChannels();
		
		sip = new Sip(asteriskManager.getManagerConnection());
		sipList = sip.getCommandSipShowPeers();
		
		//leftPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 5));
		//rightPanel.setBorder(BorderFactory.createLineBorder(Color.red, 5));
		leftPanel.setAutoscrolls(true);
		extLabelMap = new HashMap<String, JLabel>();
		extImageMap = new HashMap<String, JLabel>();
		extCallLabelMap = new HashMap<String, JLabel>();
		
		for (String[] sp : sipList){
			//System.out.println("--------->" + sp[Sip.SIP_ACCOUNT_INDEX]);			
			JPanel extPanel = new JPanel(new BorderLayout());
			JPanel intPanel = new JPanel(new BorderLayout());
			URL url = EXTENSION;
			if (sp[Sip.SIP_ACTIVE_INDEX].equals("true")) {
				intPanel.setBackground(ACTIVE_COLOR);
			} else {
				intPanel.setBackground(DEACTIVE_COLOR);
				url = EXTENSION_DISABLE;
			}
			intPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 2, true));
			extPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			
			
			extPanel.add(intPanel);
			
			JLabel lblExtension = new JLabel(sp[Sip.SIP_ACCOUNT_INDEX]);
			
			ImageIcon image  = new ImageIcon(url);			
			JLabel imgExtension = new JLabel(image);
			imgExtension.setMaximumSize(new Dimension(16, 16));
			imgExtension.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 4));
			
			extImageMap.put("extImg_" + sp[Sip.SIP_ACCOUNT_INDEX], imgExtension);
			
			extLabelMap.put("extLbl_" + sp[Sip.SIP_ACCOUNT_INDEX] , lblExtension);
			
			JLabel lblExtCall = new JLabel();
			extCallLabelMap.put("extCallLbl" + sp[Sip.SIP_ACCOUNT_INDEX], lblExtCall);
			
			intPanel.add(lblExtension);
			intPanel.add(imgExtension, BorderLayout.LINE_START);
			intPanel.add(lblExtCall, BorderLayout.LINE_END);
			
			extPanel.setBackground(new Color(255 ,255 ,255));
			leftPanel.add(extPanel);
		}
		
		
		
		rightPanel.setAutoscrolls(true);
		chnLabelMap = new HashMap<String, JLabel>();
		chnImageMap = new HashMap<String, JLabel>();
		chnCallLabelMap = new HashMap<String, JLabel>();
		for (String[] dh : dahdiList){
			JPanel extPanel = new JPanel(new BorderLayout());
			JPanel intPanel = new JPanel(new BorderLayout());
			URL url = CHANNEL;
			if (dh[Dahdi.DAHDI_STATE_INDEX].equals("true")) {
				intPanel.setBackground(ACTIVE_COLOR);
			} else {
				intPanel.setBackground(DEACTIVE_COLOR);
			}
			intPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 2, true));
			extPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			extPanel.add(intPanel);
			
			JLabel lblChannel = new JLabel(dh[Dahdi.DAHDI_FXO_INDEX]);
			
			ImageIcon image  = new ImageIcon(url);
			JLabel imgChn = new JLabel(image);
			imgChn.setMaximumSize(new Dimension(16, 16));
			imgChn.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 4));
			
			chnImageMap.put("extImg_" + dh[Dahdi.DAHDI_FXO_INDEX], imgChn);
			
			chnLabelMap.put("extLbl_" + dh[Dahdi.DAHDI_FXO_INDEX] , lblChannel);
			
			JLabel lblChnCall = new JLabel();
			chnCallLabelMap.put("extCallLbl" + dh[Dahdi.DAHDI_FXO_INDEX], lblChnCall);
			
			intPanel.add(lblChannel);
			intPanel.add(imgChn, BorderLayout.LINE_START);
			intPanel.add(lblChnCall, BorderLayout.LINE_END);
			
			extPanel.setBackground(new Color(0 ,0 ,0 ));
			rightPanel.add(extPanel);
		}
		
		rightPanel.revalidate();
		rightPanel.repaint();
		leftPanel.revalidate();
		leftPanel.repaint();
		
		CallTask();
		
	}
	
	
	private static void setDefaultLabel(boolean isCallLable, JLabel lbl){
		if (isCallLable){
			lbl.setText("");
			lbl.setToolTipText("");
		}
		
		lbl.setForeground(DEF_COLOR);
	}
	/*
	 * Set Default Color
	 */
	private static void setDefaultToLables(){
		for (java.util.Map.Entry<String, JLabel> entry : extLabelMap.entrySet()) {
			JLabel lblSip = entry.getValue();
			lblSip.setForeground(DEF_COLOR);
		}
		for (java.util.Map.Entry<String, JLabel> entry : extCallLabelMap.entrySet()){
			JLabel lblCall = entry.getValue();
			lblCall.setToolTipText("");
			lblCall.setText("");
			lblCall.setForeground(DEF_COLOR);
		}
		for (java.util.Map.Entry<String, JLabel> entry : chnLabelMap.entrySet()){
			JLabel lblChannel = entry.getValue();
			lblChannel.setForeground(DEF_COLOR);
		}
		for (java.util.Map.Entry<String, JLabel> entry : chnCallLabelMap.entrySet()){
			JLabel lblCaller = entry.getValue();
			lblCaller.setText("");
			lblCaller.setToolTipText("");
			lblCaller.setForeground(DEF_COLOR);
		}
		
	}
	
	
	/**
	 * Create the application.
	 */
	public SettingForm() {
		initialize();
		centerilizedForm();maximizeForm();
		
		
		
	}

	
	/*
	 * Maximize the Form
	 */
	private void maximizeForm(){
		frmSetting.setExtendedState(frmSetting.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}
	
	/*
	 * Set Location to Center
	 */
	private void centerilizedForm() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
		Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
		int x = ((int) rect.getMaxX()/2)  - (frmSetting.getWidth()/2) ;
		int y = ((int) rect.getMaxY()/2) - (frmSetting.getHeight()/2);
		frmSetting.setLocation(x, y);
	}
	
	
	/**
	 * Initialize the Config from
	 * @throws AsteriskAJOPExceptions 
	 */
	private static void initializeConfigForm() throws AsteriskAJOPExceptions{
		final AsteriskConfiguration config = new AsteriskConfiguration();
		config.readConfigFiles();
		final JDialog frmConfigSettings = new JDialog(frmSetting, "Config", true);
		frmConfigSettings.getContentPane().setLayout(new GridLayout(5, 1, 5, 10));
		
		URL url = SettingForm.class.getResource("/icon.png");
		ImageIcon img = new ImageIcon(url);
		frmConfigSettings.setIconImage(img.getImage());
		
		JLabel lblAsteriskServerIp = new JLabel("Asterisk Server IP Address");
		lblAsteriskServerIp.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAsteriskServerIp.setToolTipText("Asterisk Server IP Address:");
		frmConfigSettings.getContentPane().add(lblAsteriskServerIp);
		
		final JTextField txtIP = new JTextField();
		txtIP.setToolTipText("Inser Asterisk/Elastix Server IP Address");
		txtIP.setText(config.getIp());
		txtIP.setColumns(10);
		frmConfigSettings.getContentPane().add(txtIP);
		
		JLabel lblAsteriskPort = new JLabel("Asterisk Port");
		lblAsteriskPort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAsteriskPort.setToolTipText("Insert Asterisk username");
		frmConfigSettings.getContentPane().add(lblAsteriskPort);
		
		final JTextField txtPort = new JTextField();
		txtPort.setToolTipText("Insert Asterisk Server username");
		txtPort.setText(config.getPort());
		txtPort.setColumns(10);
		frmConfigSettings.getContentPane().add(txtPort);
		
		JLabel lblAsteriskManager = new JLabel("Asterisk Manager");
		lblAsteriskManager.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAsteriskManager.setToolTipText("Insert Asterisk Password or Secret");
		frmConfigSettings.getContentPane().add(lblAsteriskManager);
		
		final JTextField txtManager = new JTextField();
		txtManager.setToolTipText("Please Insert your Extension Number Here. For example 201");
		txtManager.setText(config.getManager());
		txtManager.setColumns(10);
		frmConfigSettings.getContentPane().add(txtManager);
		
		JLabel lblAsteriskSecret = new JLabel("Asterisk Secret");
		lblAsteriskSecret.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAsteriskSecret.setToolTipText("Please Insert your Extension Number Here. For example 201");
		frmConfigSettings.getContentPane().add(lblAsteriskSecret);
		
		final JPasswordField txtPass = new JPasswordField();
		txtPass.setToolTipText("Insert Asterisk Password or Secret");
		txtPass.setEchoChar('O');
		txtPass.setText(config.getSecret());
		frmConfigSettings.getContentPane().add(txtPass);
		
		JButton btnSave = new JButton("Save And Close");
		btnSave.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				config.setIp(txtIP.getText());
				config.setPort(txtPort.getText());
				config.setManager(txtManager.getText());
				config.setSecret(txtPass.getText());
				config.writeConfigFile();
				frmConfigSettings.dispose();
			}
		});
		frmConfigSettings.getContentPane().add(btnSave);
		
		/*JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmConfigSettings.dispose();
			}
		});
		frmConfigSettings.getContentPane().add(btnClose);
		*/
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		
		int x = (screenSize.width - frmConfigSettings.getWidth()) / 3;
		int y = (screenSize.height - frmConfigSettings.getHeight()) / 3;
		
		
		frmConfigSettings.pack();
		
		frmConfigSettings.setLocation(x, y);
		frmConfigSettings.setVisible(true);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSetting = new JFrame();
		frmSetting.setTitle("Asterisk Java Operator Panel (AJOP)");
		frmSetting.setResizable(false);
		frmSetting.setBounds(100, 100, 450, 300);
		frmSetting.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSetting.getContentPane().setLayout(new GridLayout(0, 2));
		URL url = SettingForm.class.getResource("/icon.png");
		ImageIcon img = new ImageIcon(url);
		frmSetting.setIconImage(img.getImage());
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		JMenuItem item = new JMenuItem("Open Config File");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timerHandle.cancel();
				try {
					initializeConfigForm();
				} catch (AsteriskAJOPExceptions e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				asteriskManager = null;
				
				try {
					initContents();
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalStateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (TimeoutException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (AsteriskAJOPExceptions e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (AuthenticationFailedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		menu.add(item);
		item = new JMenuItem("Exit");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(item);

		menu = new JMenu("Help");
		item = new JMenuItem("About");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AboutForm().frmAbout.setVisible(true);
			}
		});
		menu.add(item);
		menuBar.add(menu);
		
		frmSetting.setJMenuBar(menuBar);
		
		
		JPanel panel = new JPanel(new GridLayout(0, 2));
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		rightPanel = new JPanel(new GridLayout(0,3));
		leftPanel = new JPanel(new GridLayout(0,3));
		rightPanel.setBackground(new Color(0, 0, 0));
		JScrollPane jps = new JScrollPane(leftPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		
		JScrollPane jps2 = new JScrollPane(rightPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		panel.add(jps);
		panel.add(jps2);
		
		frmSetting.setContentPane(panel);
		
		
	}



	public void onManagerEvent(ManagerEvent event) {
		
	}

	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}



	public void onNewAgent(AsteriskAgentImpl arg0) {
		// TODO Auto-generated method stub
		
	}



	public void onNewAsteriskChannel(AsteriskChannel arg0) {
		// TODO Auto-generated method stub
		
	}



	public void onNewMeetMeUser(MeetMeUser arg0) {
		// TODO Auto-generated method stub
		
	}



	public void onNewQueueEntry(AsteriskQueueEntry arg0) {
		// TODO Auto-generated method stub
		
	}

	
	
	public static void CallTask(){
		TimerTask task = new getCoreChannel();
		Timer timer = new Timer();
		timer.schedule(task,1,1000);
		timerHandle = timer;
	}
	
	/*
	 * Timer Class to provide call information
	 */
	public static class getCoreChannel extends TimerTask {
		@Override
		public void run() {

			List<String[]> channelInfo = null;
			if (core == null){
				core = new AsteriskCore(asteriskManager.getManagerConnection());
			}
			String strChannelType = null;
			String strChannelName = null;
			String strCallerId = null;
			String strBriged = null;
			//String strDuration = null;
			String strUniqueId = null;
			boolean chkSipUniqueId = true;
			boolean chkChannelUniqueId = true;
			List<String> UniqueIdList = new ArrayList<String>();
			
			try {
				channelInfo = core.getCommandCoreShowChannelConcise();
				if (channelInfo == null) {
					setDefaultToLables();
				}
				UniqueIdList = new ArrayList<String>();
				for (String[] chn : channelInfo){
					strChannelType = utils.extractChannelType(chn[AsteriskCore.INDEX_CHANNEL]);
					strChannelName = utils.extractChannelName(chn[AsteriskCore.INDEX_CHANNEL]);
					strCallerId = chn[AsteriskCore.INDEX_CALLERID];
					strBriged = chn[AsteriskCore.INDEX_BRIGED];
					//strDuration = chn[AsteriskCore.INDEX_DURATION];
					strUniqueId = chn[AsteriskCore.INDEX_UNIQUEID];

					if (strChannelType.toLowerCase().equals("sip")){
						
						UniqueIdList.add(strUniqueId);
						
						JLabel lblSip = extLabelMap.get("extLbl_" + strChannelName);
						lblSip.setForeground(INUSE_COLOR);
						JLabel lblCall = extCallLabelMap.get("extCallLbl" + strChannelName);
						lblCall.setForeground(INUSE_COLOR);
						if (strBriged.length() < 12) {
							lblCall.setText(strBriged);
						} else {
							lblCall.setText(strBriged.substring(0,12) + "...");
							lblCall.setToolTipText(strBriged);
						}
						chkSipUniqueId = true;
						for (java.util.Map.Entry<String, String> entry : sipResultLists.entrySet()) {
							if (entry.getKey().equals(strUniqueId)) {
								chkSipUniqueId = false;
								break;
							}
						}
						if (chkSipUniqueId) {
							sipResultLists.put(strUniqueId, strChannelName);
						}
						
					} else if (strChannelType.toLowerCase().equals("dahdi")) {
						
						UniqueIdList.add(strUniqueId);
						
						JLabel lblChannel = chnLabelMap.get("extLbl_" + strChannelName);
						lblChannel.setForeground(INUSE_COLOR);
						
						JLabel lblCall = chnCallLabelMap.get("extCallLbl" + strChannelName);
						lblCall.setText(strCallerId);
						lblCall.setForeground(INUSE_COLOR);
						
						chkChannelUniqueId = true;
						for (java.util.Map.Entry<String, String> entry : channelResultLists.entrySet()) {
							if (entry.getKey().equals(strUniqueId)) {
								chkChannelUniqueId = false;
								break;
							}
						}
						if (chkChannelUniqueId) {
							channelResultLists.put(strUniqueId, strChannelName);
						}
						
					}
				}
				
				for (java.util.Map.Entry<String, String> entry : sipResultLists.entrySet()){
					JLabel lblSipCall = extCallLabelMap.get("extCallLbl" + entry.getValue());
					String strType = utils.extractChannelType(lblSipCall.getText()).toLowerCase();
					
					if (strType.equals("dahdi") ){
						JLabel lblChnCall = chnCallLabelMap.get("extCallLbl" +
							utils.extractChannelName(lblSipCall.getText())
						);
						lblSipCall.setText(lblChnCall.getText());
					} else if (strType.equals("sip")){
						String strExt = utils.extractChannelName(lblSipCall.getText());
						lblSipCall.setText(strExt);
					}
				}
				
				
				if (!isFirstTime){ 
					List<String> removeList = new ArrayList<String>();
					for (java.util.Map.Entry<String, String> entry : sipResultLists.entrySet()){
						removeList.add(entry.getKey());
					}
					
					removeList.removeAll(UniqueIdList);
					
					for (String removeItem : removeList) {
						JLabel lblSip = extLabelMap.get("extLbl_" + sipResultLists.get(removeItem));
						JLabel lblCall = extCallLabelMap.get("extCallLbl" + sipResultLists.get(removeItem));
						setDefaultLabel(false, lblSip);
						setDefaultLabel(true, lblCall);
						sipResultLists.remove(removeItem);
					}
					
					List<String> removeListChannel = new ArrayList<String>();
					for (java.util.Map.Entry<String, String> entry : channelResultLists.entrySet()){
						removeListChannel.add(entry.getKey());
					}
					removeListChannel.removeAll(UniqueIdList);
					for (String removeItem : removeListChannel) {
						JLabel lblChan = chnLabelMap.get("extLbl_" + channelResultLists.get(removeItem));
						JLabel lblCall = chnCallLabelMap.get("extCallLbl" + channelResultLists.get(removeItem));
						setDefaultLabel(false, lblChan);
						setDefaultLabel(true, lblCall);
						channelResultLists.remove(removeItem);
					}
				}
				
				isFirstTime = false;
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (TimeoutException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				AsteriskConfiguration config = new AsteriskConfiguration();
				try {
					config.readConfigFiles();
				} catch (AsteriskAJOPExceptions e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Can not connect to Asterisk Server " +
					config.getIp() + ":" + config.getPort() +
					" using '" + config.getManager() + "' username. \n" +
					"Software will try to connect after 10 seconds!"
				);
				try {
					Thread.sleep(10000);
					asteriskManager = null;
					initContents(); //Initialize Everything from the begining
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AsteriskAJOPExceptions e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AuthenticationFailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
	}
	
	
}
