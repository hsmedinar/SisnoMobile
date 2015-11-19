package com.stbig.sisnoandroid;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.stbig.sisnoandroid.data.AdapterTiendaSpinner;
import com.stbig.sisnoandroid.data.AppPreferences;
import com.stbig.sisnoandroid.data.MultiSelectionSpinner;
import com.stbig.sisnoandroid.http.HttpSisno;
import com.stbig.sisnoandroid.objects.Tienda;

public class RegisterActivity extends Activity implements OnClickListener{
	
	private EditText nombre;
	private EditText papellido;
	private EditText mapellido;
	private EditText email;
	private EditText telefono;
	private TextView tiendas;

	private Button btnregistrar;
	private Button btncontinuar;
	private ProgressDialog progressDialog;
	private AppPreferences preferences;
	private AdapterTiendaSpinner adapter;
	private Spinner sptienda;
	private MultiSelectionSpinner spinner;  
	
	private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		nombre = (EditText) findViewById(R.id.txtnombre);
		papellido = (EditText) findViewById(R.id.txtpapellido);
		mapellido = (EditText) findViewById(R.id.txtmapellido);
		email = (EditText) findViewById(R.id.txtemail);
		telefono = (EditText) findViewById(R.id.txttelefono);
		sptienda = (Spinner) findViewById(R.id.sptienda);
		tiendas = (TextView) findViewById(R.id.stienda);
		
		spinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner1);  
		
		btnregistrar = (Button) findViewById(R.id.btnregistrar);
		btncontinuar = (Button) findViewById(R.id.btncontinuar);
		
		
		btnregistrar.setOnClickListener(this);
		btncontinuar.setOnClickListener(this);
		tiendas.setOnClickListener(this);
		
		preferences = new AppPreferences(this);
		
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(preferences.getValue(getString(R.string.tag_register)).equals("1")){
			Intent i = new Intent(RegisterActivity.this, ContentActivity.class);
			startActivity(i);
		}		
	
		getValuesTienda(getString(R.string.url_tiendas));
		
	}
	
	
	private void getValues(final String url){		
		if(validateInputs()){
			
			
			progressDialog = new ProgressDialog(this);
		    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		    progressDialog.setMessage("Loading...");
		    progressDialog.setCancelable(false);
		    progressDialog.show();
		    
			Thread tr = new Thread(new Runnable() {			
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpSisno registra =  new HttpSisno(RegisterActivity.this,url);	

					Message msg = new Message();
					msg.obj=registra.sentData(parametros()); 
					enlace.sendMessage(msg);
					
				}
			});
			tr.start();			
		}				
	}	
	
	
	private Handler enlace = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub			
			int result = (int) Integer.parseInt(String.valueOf(msg.obj));
			
			progressDialog.dismiss();
			
			switch(result){
				case 200:
					preferences.saveValue(getString(R.string.tag_register), "1");
					Intent i = new Intent(RegisterActivity.this, ContentActivity.class);
					startActivity(i);
					break;
				case 0:
					break;
			}
		}		
	};
	
	private boolean validateInputs(){	
		
		if(nombre.length()<=0){
			nombre.setError(getString(R.string.error_empty_field));		   
		   return false;
		}				

		if(papellido.length()<=0){
			papellido.setError(getString(R.string.error_empty_field));		   
			   return false;
		}				

		if(mapellido.length()<=0){
			mapellido.setError(getString(R.string.error_empty_field));		   
			   return false;
		}	
		
		if(email.length()<=0){
			mapellido.setError(getString(R.string.error_empty_field));		   
			   return false;
		}	
		
		if(telefono.length()<=0){
			mapellido.setError(getString(R.string.error_empty_field));		   
			   return false;
		}			
		
		Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(email.getText().toString());
	    
	    if(!matcher.matches()){
	    	email.setError(getString(R.string.error_valid_account));
	    	return false;
	    }
	    
	    
		return true;
	}	
	
	
	private String parametros(){
		String s = spinner.getSelectedItemsAsString(); 
		String[] itemstiendas = s.toString().split(",");
		byte c=0;
		ArrayList<Tienda> lst = adapter.lista_tiendas;
		
		StringBuilder sb = new StringBuilder();  
		boolean foundOne = false;  
			
		for(byte x=0;x<=itemstiendas.length-1;x++){
				
			for(c=0;c<=lst.size()-1;c++){
								
				if(lst.get(c).getName().toString().equals(itemstiendas[x].trim())){
					
					 if (foundOne) {  
					     sb.append(", ");  
					   }  
					
					 foundOne = true;  
					 sb.append(lst.get(c).getId());  
					   
					
				}
			}
			c=0;			
		}
		   
		Log.i("result ", sb.toString());		
		
		ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();			
		values.add(new BasicNameValuePair(getString(R.string.faccion),"insertar"));
        values.add(new BasicNameValuePair(getString(R.string.fnombres),nombre.getText().toString()));	
        values.add(new BasicNameValuePair(getString(R.string.fpaterno),papellido.getText().toString()));
        values.add(new BasicNameValuePair(getString(R.string.fmaterno),mapellido.getText().toString()));        
        values.add(new BasicNameValuePair(getString(R.string.femail),email.getText().toString()));
        values.add(new BasicNameValuePair(getString(R.string.fcelular),telefono.getText().toString()));
        values.add(new BasicNameValuePair(getString(R.string.iditienda), sb.toString()));
        //values.add(new BasicNameValuePair(getString(R.string.iditienda), String.valueOf(adapter.lista_tiendas.get(sptienda.getSelectedItemPosition()).getId())));
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
	
	
	private String parametrosSpinner(){
		
		ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();		
        values.add(new BasicNameValuePair(getString(R.string.faccion),"listar"));	
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
	public void onClick(View v) {
		// TODO Auto-generated method st
		switch(v.getId()){
			
			case R.id.btnregistrar:
				getValues(getString(R.string.url_registro));				
				break;
				
			case R.id.btncontinuar:
				Intent i = new Intent(RegisterActivity.this, ContentActivity.class);
				startActivity(i);
				break;
		
		}
		
	}
	
	
    private void getValuesTienda(final String url){		

	    
			Thread tr = new Thread(new Runnable() {			
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpSisno registra =  new HttpSisno(getApplicationContext(),url);
					ArrayList<Tienda> lst = registra.listarTiendas(parametrosSpinner());
					AdapterTiendaSpinner adapter = new AdapterTiendaSpinner(getApplicationContext(), lst);
					
					Message msg = new Message();
					msg.obj = adapter;
					handlerTienda.sendMessage(msg);
					
				}
			});
			tr.start();			
	}
    
	private Handler handlerTienda = new Handler(){
		public void handleMessage(android.os.Message msg) {

			//if(msg.obj!=null){
			//	progress.dismiss();
			//}

			adapter = (AdapterTiendaSpinner)  msg.obj;
			sptienda.setAdapter(adapter);
			
			
			
			ArrayList<Tienda> lst = adapter.lista_tiendas;
			
			String[] items= new String[lst.size()];
			
			for(byte x=0;x<=lst.size() - 1 ;x++){
				items[x]=lst.get(x).getName();
			}
			
			spinner.setItems(items);
			
		};		
	};

	
}
