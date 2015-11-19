package com.stbig.sisnoandroid.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;



public class checkServices extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("servicio", "start");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i("servicio", "startcommand");
		
		GPSTracker gps = new GPSTracker(this);
		if(gps.canGetLocation()){
					
		}else{
			gps.showSettingsAlert();
		}
		
		
		return START_STICKY;
	}

	
	

}
