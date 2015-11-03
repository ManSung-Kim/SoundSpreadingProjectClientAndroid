package com.gtk.wifisoundclient;

public class StaticData {
	// lm : local member
	// spm : static private member
	// sm : static public member
	// pm : private member
	// m : public memeber
	
	// debug mode
	static public final boolean DBG = true;
	
	// server
	//192.168.0.3:9989
	static public final String IP = "192.168.0.3";
	static public final int PORT = 9989;
	
	// client status
	static public boolean IS_APP_RUN = true; // Thread 종료를 위한 flag

}
