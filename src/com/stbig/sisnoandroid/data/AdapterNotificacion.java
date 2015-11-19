package com.stbig.sisnoandroid.data;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stbig.sisnoandroid.R;
import com.stbig.sisnoandroid.objects.Notificacion;
import com.stbig.sisnoandroid.objects.Tienda;

public class AdapterNotificacion extends BaseAdapter {
	
	private Context context;
	public ArrayList<Notificacion> noti;
	
	public AdapterNotificacion(Context c,ArrayList<Notificacion> noti){
		this.context=c;
		this.noti=noti;	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stubz
		return noti.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		final Notificacion n = (Notificacion) noti.get(position);
		
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.row_list_noti, parent,false);	      
	    }		
	
		TextView txt = (TextView) view.findViewById(R.id.txtnoti);	
		txt.setText(n.getTitulo());
		
		return view;
	}

}
