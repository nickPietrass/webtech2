package webtech2.rest;

import webtech2.jpa.App;
import webtech2.jpa.GroupApp;
import webtech2.jpa.TudooApp;

public class JPAConnector {

	private static App appConnection;
	private static GroupApp groupAppConnection; 
	private static TudooApp tudooAppConnection;
	
	public static App getAppConnection() {
		if(appConnection==null) {
			appConnection = App.instance;
		}
		return appConnection;
	}
	
	public static GroupApp getGroupAppConnection() {
		if(groupAppConnection==null)
			groupAppConnection = getAppConnection().getGroupApp();
		return groupAppConnection;
	}
	
	public static TudooApp getTudooAppConnection() {
		if(tudooAppConnection==null)
			tudooAppConnection = getAppConnection().getTudooApp();
		return tudooAppConnection;
	}
	
}
