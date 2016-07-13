package org.ajop.manage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import org.ajop.gui.SettingForm;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueueEntry;
import org.asteriskjava.live.AsteriskServerListener;
import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.live.internal.AsteriskAgentImpl;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ManagerEvent;

public class AsteriskManager implements AsteriskServerListener, PropertyChangeListener, ManagerEventListener{
	
	private SettingForm settingForm;
	
	private AsteriskConfiguration asteriskConfiguration = null;
	private ManagerConnection managerConnection = null;
	
	
	public AsteriskManager(SettingForm obj) throws AsteriskAJOPExceptions, IllegalStateException, IOException, AuthenticationFailedException, TimeoutException {
		asteriskConfiguration = new AsteriskConfiguration();
		try {
			asteriskConfiguration.readConfigFiles();
		} catch (AsteriskAJOPExceptions e){
			e.printStackTrace();
		}
	//	try {
			ManagerConnectionFactory factory = new ManagerConnectionFactory(
					asteriskConfiguration.getIp(), 
					Integer.parseInt(asteriskConfiguration.getPort()),
					asteriskConfiguration.getManager(), 
					asteriskConfiguration.getSecret()
			);
			managerConnection = factory.createManagerConnection();

			managerConnection.addEventListener(this);
				
			this.settingForm = obj;
			try {
				managerConnection.login();
			}catch(TimeoutException e){
				e.printStackTrace();
			}
			managerConnection.sendAction(new StatusAction());
			
			
		//} catch (AuthenticationFailedException e) {
		//	e.printStackTrace();
		//}
		
	}
	
	public void resetManagerConnection(ManagerConnection managerConnection){
		managerConnection.logoff();
		managerConnection = null;
	}
	public ManagerConnection getManagerConnection(){
		return managerConnection;
	}
	
	public void onManagerEvent(ManagerEvent event) {
	//	System.out.println("ManagerEvent ---> " + event);
		settingForm.onManagerEvent(event);
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
	
}
