package com.stbig.sisnoandroid.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.stbig.sisnoandroid.data.AccessData;
import com.stbig.sisnoandroid.objects.Notificacion;
import com.stbig.sisnoandroid.objects.Tienda;

public class HttpSisno {
	
	private Context contexto;
	private URL url;
	private HttpURLConnection conexion;
	
	
	private JSONObject jObj = null;
	private JSONArray node = null;
	private JSONArray exito = null;
	AccessData datadb;
	
	private static final String TAG_MAIN = "notificacion";
	
	// json tienda
	private static final String TAG_TIENDA_CODIGO = "tie_Codigo";
	private static final String TAG_TIENDA_RAZON = "tie_RazonSocial";
	private static final String TAG_TIENDA_RUC = "tie_Ruc";
	private static final String TAG_TIENDA_EMAIL = "tie_Email";
	private static final String TAG_TIENDA_TELEFONO = "tie_Telefono";
	private static final String TAG_TIENDA_LATITUD = "tie_Latitud";
	private static final String TAG_TIENDA_LONGITUD = "tie_Longitud";

	// json notificacion
	private static final String TAG_NOTIFICACION_CODIGO = "not_Codigo";
	private static final String TAG_NOTIFICACION_TITULO = "not_Titulo";
	private static final String TAG_NOTIFICACION_DESCRIPCION = "not_Descripcion";
	private static final String TAG_NOTIFICACION_IMAGEN = "not_Imagen";
	private static final String TAG_NOTIFICACION_TIENDA = "tie_Codigo";
	private static final String TAG_NOTIFICACION_MEGUSTA = "not_megusta";
	
	private static final String TAG_EXITO = "exito";
	
	public HttpSisno(Context contexto,String url) {
		try{
			this.url = new URL(url);
			this.contexto = contexto;
			datadb = new AccessData(contexto);
			
		}catch(MalformedURLException e){
			
		}
	}
	
	
	 public Bitmap downloadUrl() throws IOException{
	        Bitmap bitmap=null;
	        InputStream iStream = null;
	        try{
	        	
	            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();	 
	            urlConnection.connect();	 
	            iStream = urlConnection.getInputStream();	 
	            bitmap = BitmapFactory.decodeStream(iStream);
	 
	        }catch(Exception e){
	            Log.d("Exception while downloading url", e.toString());
	        }finally{
	            iStream.close();
	        }
	        return bitmap;
	}
	
	public String sentDataWithInputStream(String data){
		
		try {
			
			StringBuilder construye = new StringBuilder();
			conexion = (HttpURLConnection) url.openConnection();			
			conexion.setRequestMethod("POST");			
			conexion.setDoInput(true);
			conexion.setDoOutput(true);		
			
			PrintWriter printer = new PrintWriter(conexion.getOutputStream());
			printer.print(data);
			printer.close();
			
			Log.i("response", String.valueOf(conexion.getResponseCode()));
			
			InputStreamReader input = new InputStreamReader(conexion.getInputStream());
			BufferedReader buffer = new BufferedReader(input);
			
			String linea;
			
			while((linea=buffer.readLine()) !=null){
				construye.append(linea);	
			}
			
			//Log.i("resultado urlconection ", " ES : " + construye.toString());
			return construye.toString();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
	} 
	
	
	public int sentData(String data){
		
		try {			
			conexion = (HttpURLConnection) url.openConnection();			
			conexion.setRequestMethod("POST");			
			conexion.setDoInput(true);
			conexion.setDoOutput(true);		
			
			PrintWriter printer = new PrintWriter(conexion.getOutputStream());
			printer.print(data);
			printer.close();
			
			
			return conexion.getResponseCode();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}		
	} 
	
	
	public ArrayList<Tienda> listarTiendas(String data){
		
		String json = sentDataWithInputStream(data);		
		ArrayList<Tienda> listaTiendas = new ArrayList<Tienda>();
		
		try{
			
			jObj = new JSONObject(json);			
			
			//if(!jObj.get(TAG_MAIN).toString().equals("-1")){
				
				node = jObj.getJSONArray(TAG_MAIN);
				
				for(int x=0; x<node.length();x++){
					
					JSONObject item = node.getJSONObject(x);					
					
					Tienda tienda = new Tienda();							
					tienda.setId(Integer.parseInt(item.getString(TAG_TIENDA_CODIGO)));
					tienda.setName(item.getString(TAG_TIENDA_RAZON));
					tienda.setRuc(item.getString(TAG_TIENDA_RUC));
					tienda.setEmail(item.getString(TAG_TIENDA_EMAIL));	
					tienda.setTelefono(item.getString(TAG_TIENDA_TELEFONO));
					tienda.setEmail(item.getString(TAG_TIENDA_EMAIL));
					tienda.setLatitud(item.getString(TAG_TIENDA_LATITUD));
					tienda.setLongitud(item.getString(TAG_TIENDA_LONGITUD));
					listaTiendas.add(tienda);					
					
					datadb.registerTienda(Integer.parseInt(item.getString(TAG_TIENDA_CODIGO)),
							item.getString(TAG_TIENDA_RAZON),
							item.getString(TAG_TIENDA_RUC), 
							item.getString(TAG_TIENDA_EMAIL),
							item.getString(TAG_TIENDA_TELEFONO),
							item.getString(TAG_TIENDA_LATITUD),
							item.getString(TAG_TIENDA_LONGITUD));
				}										
			//}
			return listaTiendas;
		}catch(Exception e){
			Log.e("Parse json", e.getMessage());
			return null;
		}
		
	}
	
	
	public ArrayList<Notificacion> listarNotificaciones(String data){
		
		String json = sentDataWithInputStream(data);	
		ArrayList<Notificacion> listaNotificacion = new ArrayList<Notificacion>();
		
		try{
			
			jObj = new JSONObject(json);			
			
			//if(!jObj.get(TAG_MAIN).toString().equals("-1")){
				
				node = jObj.getJSONArray(TAG_MAIN);
				
				datadb.deleteAllNotificacion();
				
				for(int x=0; x<node.length();x++){
					
					JSONObject item = node.getJSONObject(x);					
					
					Notificacion noti = new Notificacion();					
					noti.setCodigo(Integer.parseInt(item.getString(TAG_NOTIFICACION_CODIGO)));
					noti.setTitulo(item.getString(TAG_NOTIFICACION_TITULO));
					noti.setDescripcion(item.getString(TAG_NOTIFICACION_DESCRIPCION));
					noti.setImagen(item.getString(TAG_NOTIFICACION_IMAGEN));	
					noti.setId_tienda(item.getString(TAG_NOTIFICACION_TIENDA));
					noti.setMegusta(Integer.parseInt(item.getString(TAG_NOTIFICACION_MEGUSTA)));
					listaNotificacion.add(noti);					
					
					datadb.registerNotificacion(Integer.parseInt(item.getString(TAG_NOTIFICACION_CODIGO)),
							item.getString(TAG_NOTIFICACION_TITULO),
							item.getString(TAG_NOTIFICACION_DESCRIPCION), 
							item.getString(TAG_NOTIFICACION_IMAGEN),
							item.getString(TAG_NOTIFICACION_TIENDA),
							0);
					
					Log.i("registros", String.valueOf(x));
				}
			//}
			return listaNotificacion;
		}catch(Exception e){
			Log.e("Parse json", e.getMessage());
			return listaNotificacion;
		}
				
	}
	
	public int registerMegusta(String data){
		try{
			sentDataWithInputStream(data);
			return 1;
		}catch(Exception e){
			return 0;
		}
	}
	
	
	
}
