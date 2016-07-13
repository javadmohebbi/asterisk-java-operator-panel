package org.ajop.utils;

public class StringUtility {
	
	public String extractChannelType(String channelType){
		if (channelType.length() > 0) {
			try {
				return channelType.substring(0, channelType.indexOf("/"));
			} catch (Exception e ){
				return channelType;
			}
		} else {
			return null;
		}
	}
	public String extractChannelName(String channelName){
		if (channelName.length() > 0) {
			try {
				return channelName.substring(channelName.indexOf("/") + 1, 
						channelName.indexOf("-"));
			} catch (Exception e) {
				return channelName;
			}
		} else {
			return null;
		}
	}
}
