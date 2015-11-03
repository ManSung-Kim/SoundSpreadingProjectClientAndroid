package com.gtk.wifisoundclient.tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.gtk.wifisoundclient.util.MSLog;

public class TcpController {
	static private Socket spmSocket= null;	
	static public Socket getSocketInstacne(String ip, int port) {
		if(spmSocket==null) {
			try {
				spmSocket = new Socket(ip, port);
			} catch (UnknownHostException e) {
				MSLog.log(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				MSLog.log(e.getMessage());
				e.printStackTrace();
			}
		}
		return spmSocket;
	}
	
	static private BufferedWriter spmBuffWriter = null; // net writer
	static public BufferedWriter getBuffWriterInstance(String ip, int port) {
		if(spmBuffWriter == null) {
			try {
				spmBuffWriter = new BufferedWriter(
									new OutputStreamWriter(
											getSocketInstacne(ip, port).getOutputStream()
									)
								);
			} catch (IOException e) {
				MSLog.log(e.getMessage());
				e.printStackTrace();
			}
		}
		return spmBuffWriter;
	}
	
	static private BufferedReader spmBuffReader = null; // net reader
	static public BufferedReader getBuffReaderInstance(String ip, int port) {
		if(spmBuffReader == null) {
			try {
				spmBuffReader = new BufferedReader(
									new InputStreamReader(
											getSocketInstacne(ip, port).getInputStream()
									)
								);
			} catch (IOException e) {
				MSLog.log(e.getMessage());
				e.printStackTrace();
			}
		}
		return spmBuffReader;
	}
	
	static public void deleteAllInstance() {
		spmSocket = null;
		spmBuffWriter = null;
		spmBuffReader = null;
	}
	
}
