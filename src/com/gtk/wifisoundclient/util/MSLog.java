package com.gtk.wifisoundclient.util;

import android.util.Log;

import com.gtk.wifisoundclient.StaticData;

public class MSLog {
	private static String psmTitle = "GTK_WIFISOUND_LOG";
	
	static public <T extends Object> void log(T msg) {
		if(StaticData.DBG) Log.d(psmTitle, msg+"");
	}
}
