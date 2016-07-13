package org.ajop.manage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.response.CommandResponse;

public class Sip {
	private static String COMMAND_SIP_SHOW_PEERS = "sip show peers";
	public static int SIP_ACCOUNT_INDEX = 0;
	public static int SIP_HOST_INDEX = 1;
	public static int SIP_ACTIVE_INDEX = 3;
	
	private ManagerConnection managerConnection = null;
	
	
	public Sip(ManagerConnection managerConnection){
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
	
	public List<String[]> getCommandSipShowPeers() throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException{
		CommandAction cm = new CommandAction(COMMAND_SIP_SHOW_PEERS);
		CommandResponse cr = (CommandResponse) managerConnection.sendAction(cm);
		List<String[]> list = new ArrayList<String[]>();
		String isActive = "true";
		
		cr.getResult().remove(0); // Remove Header
		cr.getResult().remove(cr.getResult().size()-1); // Remove Footer
		
		for (String line : cr.getResult()){
			String[] lineArray = line.split("\\s+");
			if (lineArray[5].equals("0")){ // Check if port is 0, account is not active
				isActive = "false";
			} else {
				isActive = "true";
			}
			lineArray = new String[] {
				lineArray[0].split("/")[0],
				lineArray[1],
				lineArray[5],
				isActive
			};
			list.add(lineArray);
		}
		//for(String[] line : list) {
		//	System.out.println(" SIP --> " + line[SIP_ACCOUNT_INDEX] + 
		//			" --> " + line[SIP_ACTIVE_INDEX] + " --> " + line[SIP_HOST_INDEX]);
		//}
		return list;
	}
}
