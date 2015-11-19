package com.stbig.sisnoandroid.data;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stbig.sisnoandroid.R;
import com.stbig.sisnoandroid.objects.Tienda;

public class AdapterTiendaSpinner  extends BaseAdapter {
	
	private Context context;
	public ArrayList<Tienda> lista_tiendas;
	
	public AdapterTiendaSpinner(Context c,ArrayList<Tienda> lista_tiendas){
		this.context=c;
		this.lista_tiendas=lista_tiendas;	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stubz
		return lista_tiendas.size();
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
		
		final Tienda t = (Tienda) lista_tiendas.get(position);
		
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.row_spinner_register, parent,false);	      
	    }		
	
		TextView txt = (TextView) view.findViewById(R.id.txttienda);	
		txt.setText(t.getName());
		
		return view;
	}

}
