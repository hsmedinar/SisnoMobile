package com.stbig.sisnoandroid.fragments;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.stbig.sisnoandroid.DetalleNotificacion;
import com.stbig.sisnoandroid.NotiTiendas;
import com.stbig.sisnoandroid.R;
import com.stbig.sisnoandroid.data.AdapterTienda;
import com.stbig.sisnoandroid.http.HttpSisno;
import com.stbig.sisnoandroid.objects.Notificacion;
import com.stbig.sisnoandroid.objects.Tienda;

public class FragmentTiendas extends Fragment implements OnRefreshListener,OnItemClickListener {
	
	private ListView lststores;
	private LinearLayout emptydata;
	private	PullToRefreshLayout mPullToRefreshLayout;
	private AdapterTienda adapter;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.activity_tiendas, container, false);
         
        lststores = (ListView) rootView.findViewById(R.id.lststores);
        emptydata = (LinearLayout) rootView.findViewById(R.id.empty_data);        
        lststores.setOnItemClickListener(this);
        
        return rootView;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onViewCreated(view, savedInstanceState);
    	
    	ViewGroup viewGroup = (ViewGroup) view;

        // As we're using a ListFragment we create a PullToRefreshLayout manually
        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

        // We can now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())
                // We need to insert the PullToRefreshLayout into the Fragment's ViewGroup
                .insertLayoutInto(viewGroup)
                // Here we mark just the ListView and it's Empty View as pullable
                .theseChildrenArePullable(R.id.lststores, android.R.id.empty)
                .listener(this)
                .setup(mPullToRefreshLayout);
        
    }
    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onActivityCreated(savedInstanceState);    	    	
    }
    
    @Override
    public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	getValues(getString(R.string.url_tiendas));    	
    }
    
    
    private void getValues(final String url){		

		    
			Thread tr = new Thread(new Runnable() {			
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpSisno registra =  new HttpSisno(getActivity().getApplicationContext(),url);
					ArrayList<Tienda> lst = registra.listarTiendas(parametros());
					AdapterTienda adapter = new AdapterTienda(getActivity(), lst);
					
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

			adapter = (AdapterTienda)  msg.obj;
			lststores.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			mPullToRefreshLayout.setRefreshComplete();
			
			if(adapter.getCount()!=0){
				emptydata.setVisibility(View.GONE);	
			}else{
				emptydata.setVisibility(View.VISIBLE);
			}
			
						
		};		
	};
	
    
    private String parametros(){
		
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
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		getValues(getString(R.string.url_tiendas));
	}
    
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Tienda t = (Tienda)  adapter.tiendas.get(arg2);
		Intent intent = new Intent(getActivity(), NotiTiendas.class);		
		intent.putExtra("code", String.valueOf(t.getId()));		
		startActivity(intent);
		
	}
	
	
	
}
