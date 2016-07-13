package org.ajop.manage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.response.CommandResponse;

public class Dahdi {
	private static String COMMAND_DAHDI_SHOW_CHANNELS = "dahdi show channels";
	public static int DAHDI_FXO_INDEX = 0;
	public static int DAHDI_CONTEXT_INDEX = 1;
	public static int DAHDI_STATE_INDEX = 2;
	
	private ManagerConnection managerConnection = null;
	
	
	public Dahdi(ManagerConnection managerConnection){
		this.managerConnection = managerConnection;
	}
	
	/*
	 * Set Manager Connection
	 */
	public void setManagerConnection(ManagerConnection managerConnection){
		this.managerConnection = managerConnection;
	}
	/*
	 * Get Manager Connection
	 */
	public ManagerConnection getManagerConnection(){
		return this.getManagerConnection();
	}
	
	public List<String[]> getCommandDahdiShowChannels() throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException{
		CommandAction cm = new CommandAction(COMMAND_DAHDI_SHOW_CHANNELS);
		CommandResponse cr = (CommandResponse) managerConnection.sendAction(cm);
		List<String[]> list = new ArrayList<String[]>();
		String isActive = "true";
		
		cr.getResult().remove(0); // Remove Header
		cr.getResult().remove(0); // Remove Header
		
		for (String line : cr.getResult()){
			String[] lineArray = line.split("\\s+");
			
			if (lineArray[5].equals("In")){ // Check if port is 0, account is not active
				isActive = "true";
			} else {
				isActive = "false";
			}
			lineArray = new String[] {
				lineArray[1],
				lineArray[2],
				isActive
			};
			list.add(lineArray);
		}
		//for(String[] line : list) {
		//	System.out.println(" DAHDI --> " + line[DAHDI_FXO_INDEX] + 
		//			" --> " + line[DAHDI_CONTEXT_INDEX] + " --> " + line[DAHDI_STATE_INDEX]);
		//}
		return list;
	}
	
}
