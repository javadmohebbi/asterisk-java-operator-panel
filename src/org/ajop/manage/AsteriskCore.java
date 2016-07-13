package org.ajop.manage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.response.CommandResponse;

public class AsteriskCore {
	
	private static String COMMAND_CORE_SHOW_CHANNELS_CONCISE = "core show channels concise";
	public static int INDEX_CHANNEL = 0;
	public static int INDEX_CONTEXT = 1;
	public static int INDEX_EXTEN = 2;
	public static int INDEX_PRIORITY = 3;
	public static int INDEX_STATS = 4;
	public static int INDEX_APPLICATION = 5;
	public static int INDEX_DATA = 6;
	public static int INDEX_CALLERID = 7;
	//public static int INDEX_ACCOUNTCODE = 8;
	//public static int INDEX_AMAFLAGS = 9;
	public static int INDEX_PEERACCOUNT = 10;
	public static int INDEX_DURATION = 11;
	public static int INDEX_BRIGED = 12;
	public static int INDEX_UNIQUEID = 13;
	
	private ManagerConnection managerConnection = null;
	
	public AsteriskCore(ManagerConnection managerConection){
		this.managerConnection = managerConection;
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
	
	
	public List<String[]> getCommandCoreShowChannelConcise() throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException{
		CommandAction cm = new CommandAction(COMMAND_CORE_SHOW_CHANNELS_CONCISE);
		CommandResponse cr = (CommandResponse) managerConnection.sendAction(cm);
		List<String[]> list = new ArrayList<String[]>();		
		
		/*
		 * SIP/222-000005bd!macro-dial-one!s!37!Ring!Dial!SIP/201,"",tr!222!!!3!2!(None)!1468068861.2946
		 * SIP/201-000005be!support!201!1!Ringing!AppDial!(Outgoing Line)!201!!!3!2!(None)!1468068861.2947
		 * Channel:Context:Exten:Priority:Stats:Application:Data:CallerID:Accountcode:Amaflags:Duration:Bridged 
		 */
		
		for (String line : cr.getResult()){
			String[] lineArray = line.split("!");
			
			lineArray = new String[] {
				lineArray[INDEX_CHANNEL], //Channel 0
				lineArray[INDEX_CONTEXT], //Context 1
				lineArray[INDEX_EXTEN], //Exten 2
				lineArray[INDEX_PRIORITY], //Priority 3
				lineArray[INDEX_STATS], //Stats 4
				lineArray[INDEX_APPLICATION], //Application 5
				lineArray[INDEX_DATA], //Data 6
				lineArray[INDEX_CALLERID], //CalledId 7
				"", // 8
				"", // 9
				lineArray[INDEX_PEERACCOUNT], //AccountCode 10
				lineArray[INDEX_DURATION], //Duration 11
				lineArray[INDEX_BRIGED], //Briged 12
				lineArray[INDEX_UNIQUEID] //Unique Id 13			
			};
			list.add(lineArray);
			//System.out.println("---> Count" + lineArray.length);
		}
		for(String[] line : list) {
			String result = "";
			for(int i=0; i < line.length ; i++) {
				result = result +  i + " -> "  + line[i] + " \t ";
			}
				
			//System.out.println(" CORE --> " + result);
			
		}
		return list;
	}

}
