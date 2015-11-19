package com.stbig.sisnoandroid;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stbig.sisnoandroid.data.AccessData;
import com.stbig.sisnoandroid.data.AdapterNotificacion;
import com.stbig.sisnoandroid.http.HttpSisno;
import com.stbig.sisnoandroid.objects.Notificacion;

public class DetalleNotificacion extends Activity implements OnClickListener {
	
	TextView titulo;
	TextView descripcion;
	HttpSisno conexion;
	ImageView iView;
	ProgressDialog progressDialog;
	ProgressBar load;
	ImageButton megusta;
	String code;
	AccessData datadb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_notificacion);
		titulo = (TextView) findViewById(R.id.txttitulod);
		descripcion = (TextView) findViewById(R.id.txtdescripciond);	
		iView = (ImageView) findViewById(R.id.img_noti);
		load = (ProgressBar) findViewById(R.id.load);
		megusta = (ImageButton) findViewById(R.id.imgmegusta);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		datadb = new AccessData(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		megusta.setOnClickListener(this);
		
		Bundle v = getIntent().getExtras();
		if(v!=null){
			code = v.getString("code");
			titulo.setText(v.getString("title"));
			descripcion.setText(v.getString("descripcion"));			
			conexion = new HttpSisno(this, getString(R.string.url_imagen) + v.getString("code") + "/" + v.getString("imagen") );
								
			if(v.getString("megusta").equals("1")){
				megusta.setBackgroundResource(R.drawable.ic_megusta);
				megusta.setEnabled(false);
			}else{
				megusta.setBackgroundResource(R.drawable.ic_no_megusta);
				megusta.setEnabled(true);
			}
			
			load.setVisibility(View.VISIBLE);
			iView.setVisibility(View.GONE);
			DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute();
            
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    
		switch (item.getItemId()) {
	    	
			case android.R.id.home:
	    		Intent intent = new Intent(DetalleNotificacion.this,ContentActivity.class);
	    		startActivity(intent);	        	    	
	    		return true;
	    	}
	    
		return super.onOptionsItemSelected(item);
	}
	
    private class DownloadTask extends AsyncTask<String, Integer, Bitmap>{
        Bitmap bitmap = null;
        @Override
        protected Bitmap doInBackground(String... url) {
            try{
                bitmap = conexion.downloadUrl();
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return bitmap;
        }
 
        @Override
        protected void onPostExecute(Bitmap result) {
        	
            iView.setImageBitmap(result);
            iView.setVisibility(View.VISIBLE);
            load.setVisibility(View.GONE);
            //progressDialog.dismiss();
            
        }
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.imgmegusta:
				registerMegusta(getString(R.string.url_notificaciones));				
				break;
		}
	}
	
	
    private void registerMegusta(final String url){		
	    
	   	 progressDialog = new ProgressDialog(DetalleNotificacion.this);
	     progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	     progressDialog.setMessage("Loading...");
	     progressDialog.setCancelable(false);
	     progressDialog.show();	
     
			Thread tr = new Thread(new Runnable() {			
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpSisno registra =  new HttpSisno(DetalleNotificacion.this,url);
					int result = registra.registerMegusta(parametros(code));										
					
					Message msg = new Message();
					msg.obj = result;
					handler.sendMessage(msg);
					
				}
			});
			tr.start();			
	}
    
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {

			int v= Integer.parseInt(msg.obj.toString());
			String[] ids = {code};
			if(v==1){
				datadb.updateMegusta(ids, "1");
				megusta.setBackgroundResource(R.drawable.ic_megusta);
			}else{
				megusta.setBackgroundResource(R.drawable.ic_no_megusta);			
			}
			
			progressDialog.dismiss();
		};		
	};

	
	
    private String parametros(String codigo){
		
		ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();		
        values.add(new BasicNameValuePair(getString(R.string.faccion),"insertarmegusta"));
        values.add(new BasicNameValuePair(getString(R.string.fnoticodigo),codigo));
        values.add(new BasicNameValuePair(getString(R.string.fmegusta),"1"));
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
	
    
}

