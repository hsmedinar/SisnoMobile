package com.stbig.sisnoandroid.services;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.stbig.sisnoandroid.ContentActivity;
import com.stbig.sisnoandroid.DetalleNotificacion;
import com.stbig.sisnoandroid.R;
import com.stbig.sisnoandroid.data.AccessData;
import com.stbig.sisnoandroid.objects.Notificacion;
import com.stbig.sisnoandroid.objects.Tienda;

public class GPSTracker extends Service implements LocationListener {
 
    private final Context mContext;
 
    // flag for GPS status
    boolean isGPSEnabled = false;
 
    // flag for network status
    boolean isNetworkEnabled = false;
 
    // flag for GPS status
    boolean canGetLocation = false;
 
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    
    private AccessData datadb;
 
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
 
    // The minimum time between updates in milliseconds
    //private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private static final long MIN_TIME_BW_UPDATES = 0; // 1 minute
 
    // Declaring a Location Manager
    protected LocationManager locationManager;
 
    public GPSTracker(Context context) {
        this.mContext = context;
        datadb = new AccessData(context);
        getLocation();
    }
 
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
 
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGPSEnabled && !isNetworkEnabled) {
                
            	// no network provider is enabled
            	
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return location;
    }
     
    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }       
    }
     
    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
         
        // return latitude
        return latitude;
    }
     
    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
         
        // return longitude
        return longitude;
    }
     
    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
     
    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
      
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
  
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
  
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
  
        // Showing Alert Message
        alertDialog.show();
    }
 
    @Override
    public void onLocationChanged(Location location) {
    	//Toast.makeText(mContext, "lat : " +  location.getLatitude()
		//		+ "long : " + location.getLongitude()
		//		, Toast.LENGTH_LONG).show();
    	calculateDistance(location.getLatitude(),location.getLongitude());
    }
 
    @Override
    public void onProviderDisabled(String provider) {
    }
 
    @Override
    public void onProviderEnabled(String provider) {
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
 
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    
    private void calculateDistance(double lat, double longi){
    	
    	Location my_location=new Location("myLocation");
        my_location.setLatitude(lat);
        my_location.setLongitude(longi);
        
        ArrayList<Tienda> tiendas = datadb.listarUbicacionTienda();
        
        for(byte x=0;x<=tiendas.size()-1 ;x++){
        	
        	Tienda t = (Tienda) tiendas.get(x);
        	
        	Location location_tienda=new Location("tiendaLocation");
        	location_tienda.setLatitude(Double.parseDouble(t.getLatitud()));
        	location_tienda.setLongitude(Double.parseDouble(t.getLongitud()));
            
            double distance=location_tienda.distanceTo(my_location);
            
            if(distance<=100){
            	
            	//Log.i("distancia", " es : " + distance);
            	//Log.i("codigo tienda ", " es : " + String.valueOf(t.getId()));
            	
            	int value = datadb.getValuesOferta(String.valueOf(t.getId()));
            	
            	if(value!=0){  
            		
            		ArrayList<Notificacion> noti = datadb.listaNotificacionesxTienda(String.valueOf(t.getId()));
            		
            		for(byte v=0;v<=noti.size()-1;v++){            			
            			Notificacion n = (Notificacion) noti.get(v);            			
            			Log.i("titulo noti ", n.getTitulo());
                		notificaciones(n.getCodigo(),n.getTitulo(),n.getDescripcion(),n.getImagen(),String.valueOf(n.getMegusta()));	
            		}
            	}
            	
            }
        }
    	
    }
    
	NotificationManager notimanager;
    Notification noti;    
    Intent intent;        
    PendingIntent contIntent;
    
    private void notificaciones(int code,String title, String body,String imagen, String megusta){
    	
    	
		notimanager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
		
		noti = new Notification(R.drawable.icon_store, title, System.currentTimeMillis());
			
		intent = new Intent(mContext,DetalleNotificacion.class);
		
		intent.putExtra("code", String.valueOf(code));
		intent.putExtra("title", title);
		intent.putExtra("descripcion", body);
		intent.putExtra("imagen", imagen);
		intent.putExtra("megusta", megusta);
		
		contIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
		
		noti.setLatestEventInfo(mContext, title, body, contIntent);
		
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		
		notimanager.notify(code, noti);
    
    }
}