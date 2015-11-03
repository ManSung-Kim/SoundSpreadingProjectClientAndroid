package com.gtk.wifisoundclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.gtk.wifisoundclient.tcp.TcpController;
import com.gtk.wifisoundclient.util.MSLog;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends Activity {

	private Handler pmHandler = null;
	private Socket pmSocket = null;
	private BufferedWriter pmNetBuffWriter = null;
	private BufferedReader pmNetBuffReader = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MSLog.log("onCreate");
		
		StaticData.IS_APP_RUN = true;
		
		pmHandler = new Handler();
		
		// net setup
		new Thread(new Runnable() {
			public void run() {
				pmSocket = TcpController.getSocketInstacne(StaticData.IP, StaticData.PORT);
				pmNetBuffWriter = TcpController.getBuffWriterInstance(StaticData.IP, StaticData.PORT);
				pmNetBuffReader = TcpController.getBuffReaderInstance(StaticData.IP, StaticData.PORT);				
			
				if( pmSocket==null || pmNetBuffWriter==null || pmNetBuffReader==null) {
					MSLog.log("soc|bufW|bufR null. fail");
					return;
				}

				}
		}).start();
		
		// spin lock, wait for instances
		while(true) {
			if( pmSocket==null || pmNetBuffWriter==null || pmNetBuffReader==null) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				break;
			}
		}
		
		// communicater thread
		pmNetReadThread.start();
		
		// test data send
		PrintWriter lmPrintWriter = new PrintWriter(pmNetBuffWriter, true); // auto flush true
		lmPrintWriter.println("This message sended from client");				
	
	}
	
	private Thread pmNetReadThread = new Thread() {
		public void run() {
			try {
				String lmStrRead;
				MSLog.log("Network Read Thread Start");
				while(StaticData.IS_APP_RUN) {
					MSLog.log("Network Read Thread Wait For Message");
					do{ // spin lock
						Thread.sleep(50);
						lmStrRead = pmNetBuffReader.readLine();
					} while(lmStrRead==null && StaticData.IS_APP_RUN );
					//lmStrRead = pmNetBuffReader.readLine();
					
					// 여기서 handler로 던지면 앱에서 처리
					pmHandler.post(pmHandleRunnable);
					MSLog.log("Get New Message : " + lmStrRead);
				}
				//disconnectSocket();
			} catch (Exception e) {
				MSLog.log(e.getMessage());
				e.printStackTrace();				
			}
		}
	};
	private Runnable pmHandleRunnable = new Runnable() {
		public void run() {
			// do 
		}
	};
	
	private void disconnectSocket() {
		try {
			if(pmSocket!=null) {
				pmSocket.close();
			} 
			pmSocket = null;
		} catch (IOException e ) {
			MSLog.log(e.getMessage());
			e.printStackTrace();
		} finally {
			MSLog.log("client socket close complete");
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MSLog.log("onDestroy");
		
		disconnectSocket();		
		TcpController.deleteAllInstance(); // 왜 이거 안하면 다음번 메시지를 못받지..
		StaticData.IS_APP_RUN = false; // for thread stop
	}

}
