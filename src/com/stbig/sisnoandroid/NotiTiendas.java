package com.stbig.sisnoandroid;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.stbig.sisnoandroid.data.AdapterNotificacion;
import com.stbig.sisnoandroid.http.HttpSisno;
import com.stbig.sisnoandroid.objects.Notificacion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NotiTiendas extends Activity implements OnItemClickListener {
	
	private ListView lista;
	private AdapterNotificacion adapter;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noti_tiendas);
		lista = (ListView) findViewById(R.id.listnotitienda);
		lista.setOnItemClickListener(this);        
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Bundle v = getIntent().getExtras();
		if(v!=null){
			Toast.makeText(this,  v.getString("code"), Toast.LENGTH_LONG).show();
			getValues(getString(R.string.url_notificaciones),v.getString("code"));	
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    
		switch (item.getItemId()) {
	    	
			case android.R.id.home:
	    		Intent intent = new Intent(NotiTiendas.this,ContentActivity.class);
	    		startActivity(intent);	        	    	
	    		return true;
	    	}
	    
		return super.onOptionsItemSelected(item);
	}
	
	private void getValues(final String url,final String idtienda){		

		progressDialog = new ProgressDialog(this);
    	progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	progressDialog.setMessage("Loading...");
    	progressDialog.setCancelable(false);
    	progressDialog.show();
    
		Thread tr = new Thread(new Runnable() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpSisno registra =  new HttpSisno(getApplicationContext(),url);
				ArrayList<Notificacion> lst = registra.listarNotificaciones(parametros(idtienda));					
				AdapterNotificacion adapter = new AdapterNotificacion(NotiTiendas.this, lst);
				
				Message msg = new Message();
				msg.obj = adapter;
				handler.sendMessage(msg);
				
			}
		});
		tr.start();			
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {

			//if(msg.obj!=null){
			//	progress.dismiss();
			//}
			
			progressDialog.dismiss();
			
			adapter = (AdapterNotificacion)  msg.obj;
			if(adapter.getCount()!=0){
				lista.setAdapter(adapter);				
			}
						
		};		
	};
	
    
    private String parametros(String idtienda){
		
		ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();		
        values.add(new BasicNameValuePair(getString(R.string.faccion),"listar"));
        values.add(new BasicNameValuePair(getString(R.string.ftie_Codigo),idtienda));
        String param="";

        try {	       	 
	       	 for (NameValuePair nvp : values) {       		 
	       		 if (param == "")
						param=nvp.getName() + "=" + URLEncoder.encode(nvp.getValue(),"UTF-8");								
	       		  else 
	       			param+= "&" + nvp.getName() + "=" + URLEncoder.encode(nvp.getValue(),"UTF-8");       		 
	       	 }
    	} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}
        
        return param;
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Notificacion noti = (Notificacion)  adapter.noti.get(arg2);
		Intent intent = new Intent(NotiTiendas.this, DetalleNotificacion.class);
		intent.putExtra("code", String.valueOf(noti.getCodigo()));
		intent.putExtra("title", noti.getTitulo());
		intent.putExtra("descripcion", noti.getDescripcion());
		intent.putExtra("imagen", noti.getImagen());
		intent.putExtra("megusta", String.valueOf(noti.getMegusta()));
		startActivity(intent);
		
	}
	
	
}
