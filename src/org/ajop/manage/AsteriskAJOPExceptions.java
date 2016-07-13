package org.ajop.manage;

public class AsteriskAJOPExceptions extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	public static final String READ_CONFIG_FILE = "Config file can not be read";
	public static final String INITIALIZATION_FAILED = "Can not initialized config file";
	public static final String INVALID_IP_ADDRESS = "IP address is invalid";
	
	public AsteriskAJOPExceptions(String msg){
		super(msg);
	}
	
}
