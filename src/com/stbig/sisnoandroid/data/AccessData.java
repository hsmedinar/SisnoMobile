package com.stbig.sisnoandroid.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.stbig.sisnoandroid.R;
import com.stbig.sisnoandroid.objects.Notificacion;
import com.stbig.sisnoandroid.objects.Tienda;

public class AccessData extends SQLiteOpenHelper {
	
	private static final int version = 1;
    private SQLiteDatabase db;
    Context context;

	public AccessData(Context context) {		
		super(context, context.getString(R.string.db), null, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.beginTransaction();

	      try {
	           
	        db.execSQL("CREATE TABLE tiendas(id Integer,name TEXT,ruc TEXT,email TEXT,telefono TEXT,latitud TEXT, longitud TEXT)");	         	
	        db.execSQL("CREATE TABLE notificacion(id Integer,titulo TEXT,descripcion TEXT,imagen TEXT,cod_tienda TEXT,megusta Integer)");	        
	        db.setTransactionSuccessful();
	            
	      } catch (SQLException e) {
	        throw e;
	      } finally {
	        db.endTransaction();
	      }
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	
    public void registerTienda(int id,String name, String ruc, String email, String telefono, String latitud, String longitud) throws SQLException {
        try {
        	
        	if(validateRegister(name)!=1){
                db = getReadableDatabase();
                ContentValues values = new ContentValues();
                values.put("id", id);
                values.put("name", name);
                values.put("ruc", ruc);
                values.put("email", email);
                values.put("telefono", telefono);
                values.put("latitud", latitud);
                values.put("longitud", longitud);
                db.insert("tiendas", null, values);
                db.close();        		
        	}
        		
        } catch (SQLException e) {
            
        }
    }
    
    public void registerNotificacion(int id,String titulo, String descripcion, String imagen, String cod_tienda,int megusta) throws SQLException {
        try {
            db = getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("id", id);
            values.put("titulo", titulo);
            values.put("descripcion", descripcion);
            values.put("imagen", imagen);
            values.put("cod_tienda", cod_tienda);
            values.put("megusta", megusta);
            db.insert("notificacion", null, values);
            db.close();
        } catch (SQLException e) {
            
        }
    }
    
    
    public void deleteAllNotificacion(){
    	
        db = getReadableDatabase();       
        db.rawQuery("delete from notificacion", null);
        db.close();
            
    }
	
    
    public ArrayList<Tienda> listarUbicacionTienda(){
    	
    	ArrayList<Tienda> values = new ArrayList<Tienda>();
    	
        db = getReadableDatabase();
        
        Cursor c = db.rawQuery("select * from tiendas order by id desc", null);
        
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
            	Tienda data = new Tienda(c.getInt(0), c.getString(1), c.getString(2), c.getString(3),c.getString(4),c.getString(5),c.getString(6));
                values.add(data);
            } while (c.moveToNext());
        }
        
        db.close();
        c.close();
        
        return values;    
    }
    
    
    public int validateRegister(String name){
    	
    	int result=0;
        db = getReadableDatabase();
        
        Cursor c = db.rawQuery("select * from tiendas where name='" + name +"'", null);
        
        result=c.getCount();
        db.close();
        c.close();        
        return result;
        
    }
    
    
    public int getValuesOferta(String cod_tienda){
    	
    	int value =0;
        db = getReadableDatabase();        
        Cursor c = db.rawQuery("select * from notificacion where cod_tienda='"+ cod_tienda + "'", null);        
        value = c.getCount();
        db.close();
        c.close();        
        return value;        
    }
 
 
    public Notificacion getOfertaTienda(String cod_tienda){
    	
    	
    	Notificacion noti=null;
    	
        db = getReadableDatabase();
        
        Cursor c = db.rawQuery("select * from notificacion where cod_tienda='"+ cod_tienda + "'", null);
        
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
            	noti = new Notificacion();
            	noti.setCodigo(c.getInt(0));
            	noti.setTitulo(c.getString(1));
            	noti.setDescripcion(c.getString(2));
            	noti.setImagen(c.getString(3));
            	noti.setId_tienda(c.getString(4));
            	noti.setMegusta(c.getInt(5)); 
            } while (c.moveToNext());
        }
        
        db.close();
        c.close();
        return noti;
        
    }
    
    
    public ArrayList<Notificacion> listaNotificacionesxTienda(String cod_tienda){
    	
    	ArrayList<Notificacion> lista = new ArrayList<Notificacion>();
    	
    	
        db = getReadableDatabase();
        
        Cursor c = db.rawQuery("select * from notificacion where cod_tienda='"+ cod_tienda + "'", null);
        
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
            	Notificacion noti = new Notificacion();
            	noti.setCodigo(c.getInt(0));
            	noti.setTitulo(c.getString(1));
            	noti.setDescripcion(c.getString(2));
            	noti.setImagen(c.getString(3));
            	noti.setId_tienda(c.getString(4));   
            	noti.setMegusta(c.getInt(5)); 
            	lista.add(noti);
            } while (c.moveToNext());
        }
        
        db.close();
        c.close();
        return lista;
        
    }
    
    
    public ArrayList<Notificacion> listaNotificacionesTodas(){
    	
    	ArrayList<Notificacion> lista = new ArrayList<Notificacion>();
    	
    	
        db = getReadableDatabase();
        
        Cursor c = db.rawQuery("select * from notificacion", null);
        
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
            	Notificacion noti = new Notificacion();
            	noti.setCodigo(c.getInt(0));
            	noti.setTitulo(c.getString(1));
            	noti.setDescripcion(c.getString(2));
            	noti.setImagen(c.getString(3));
            	noti.setId_tienda(c.getString(4));   
            	noti.setMegusta(c.getInt(5)); 
            	lista.add(noti);
            } while (c.moveToNext());
        }
        
        db.close();
        c.close();
        return lista;
        
    }

    
    
    public int getNotixTiendaTotal(String cod_tienda){
    	    	
    	int total=0;    	
        db = getReadableDatabase();        
        Cursor c = db.rawQuery("select * from notificacion where cod_tienda='"+ cod_tienda + "'", null);        
        total=c.getCount();        
        db.close();
        c.close();
        return total;
        
    }
    
    
    public void updateMegusta(String[] id,String valor){    	
   	 db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("megusta", valor);
        db.update("notificacion", values, "id=?", id);
        db.close();
   }


}
