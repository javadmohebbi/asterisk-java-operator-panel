package org.ajop.manage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class AsteriskConfiguration {
	
	private String ip = null;
	private String port = "5038";
	private String manager = null;
	private String secret = null;
		
	
	/*
	 * Set Server IP address
	 */
	public void setIp(String ip){
		this.ip = ip;
	}
	/*
	 * Set Server port number
	 */
	public void setPort(String port){
		this.port = port;
	}
	/*
	 * Set manager user name 
	 */
	public void setManager(String manager){
		this.manager = manager;
	}
	/*
	 * Set manager secret
	 */
	public void setSecret(String secret){
		this.secret = secret;
	}
	/*
	 * Get Server IP address
	 */
	public String getIp(){
		return this.ip;
	}
	/*
	 * Get Server port
	 */
	public String getPort(){
		return this.port;
	}
	/*
	 * Get manager user name
	 */
	public String getManager(){
		return this.manager;
	}
	/*
	 * Get manager secret
	 */
	public String getSecret(){
		return this.secret;
	}
	
	
	/*
	 * Constructor - Read config files and set local variables
	 */
	public AsteriskConfiguration(){
		
	}
	
	/*
	 * Write key,value pairs to config.properties
	 */
	public void writeConfigFile(){
		Properties prop = new Properties();
		OutputStream output = null;
		try {
			output = new FileOutputStream("config.properties");
			prop.setProperty("ip", getIp());
			prop.setProperty("port", getPort());
			prop.setProperty("manager", getManager());
			prop.setProperty("secret", getSecret());
			prop.store(output, null);
		} catch (IOException io){
			io.printStackTrace();
		}
		
	}
	
	/*
	 * Read key,value from config.properties
	 */
	public void readConfigFiles() throws AsteriskAJOPExceptions {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			
			try {
				prop.load(input);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block				
				e.printStackTrace();
			}
			setIp(prop.getProperty("ip"));
			setPort(prop.getProperty("port"));
			setManager(prop.getProperty("manager"));
			setSecret(prop.getProperty("secret"));
			try {
				if (getIp().length() == 1 
						|| getManager().length() == 0
						|| getSecret().length() == 0
					) {
					throw new AsteriskAJOPExceptions(AsteriskAJOPExceptions.READ_CONFIG_FILE);
				}
			} catch (Exception e){
				throw new AsteriskAJOPExceptions(AsteriskAJOPExceptions.INITIALIZATION_FAILED);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
