package com.stbig.sisnoandroid.services;

import org.json.JSONObject;

import com.stbig.sisnoandroid.R;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class checkDistancia extends BroadcastReceiver {

	private static final int NOTIF_ALERTA_ID = 1;
	Context contexto;
	public static int situacion=0;

	@Override
	public void onReceive(Context context, Intent intent) {
		 
         Bundle extras = intent.getExtras();
         contexto=context;
         
         if(extras != null ){        
            new asyncenviar().execute();           
         }
   } 
	
	
	public void SetAlarm(Context context){
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, checkDistancia.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 30 seconds
        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 30 , pi);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 86400000 , pi);        
    }

	
    public void CancelAlarm(Context context){
        Intent intent = new Intent(context, checkDistancia.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

   
    
 // CLASE ASINCRONA DE ENVIO DE POSICION   
    class asyncenviar extends AsyncTask< String, String, String > {
   	 
        protected void onPreExecute() {
        	
        }
 
		protected String doInBackground(String... params) {
		
			try{	
			
				
				
			     return "ok";
			}catch(Exception e){
					return "err";
			}
        }
		
		protected void onPostExecute(String result) {
           
			
           if (result.equals("ok")){
        	   
        	   
        	   /*
        		NotificationCompat.Builder mBuilder =
				        new NotificationCompat.Builder(contexto)
				        .setSmallIcon(R.drawable.ic_launcher)
				        .setSmallIcon(R.drawable.ic_launcher)
				        .setContentTitle("NUEVO MENSAJE")
				        .setContentText(recid.getTexto_MEN())
				        .setContentInfo(recid.getFin_MEN())
				        .setTicker("UGEL");
        		Vibrator vibrator =(Vibrator) contexto.getSystemService(Context.VIBRATOR_SERVICE);
        	    vibrator.vibrate(200);
				Intent notIntent = 	new Intent(contexto, Content.class);
				notIntent.putExtra("Texto_MEN", recid.getTexto_MEN());
				PendingIntent contIntent = PendingIntent.getActivity(contexto, 0, notIntent, 0);
				
			    mBuilder.setContentIntent(contIntent);
				
				NotificationManager mNotificationManager =(NotificationManager)contexto.getSystemService(Context.NOTIFICATION_SERVICE);

				mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
        	   */
        	   
   			}else{
   				
   					//Toast toast1 = Toast.makeText(contexto.getApplicationContext(),"OCURRIO UN ERROR", Toast.LENGTH_SHORT);
   	           	    //toast1.show();	
   				
           }
          }
	    }
}