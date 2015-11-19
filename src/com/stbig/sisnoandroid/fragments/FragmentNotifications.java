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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.stbig.sisnoandroid.DetalleNotificacion;
import com.stbig.sisnoandroid.R;
import com.stbig.sisnoandroid.data.AdapterNotificacion;
import com.stbig.sisnoandroid.http.HttpSisno;
import com.stbig.sisnoandroid.objects.Notificacion;

public class FragmentNotifications extends Fragment implements OnRefreshListener,OnItemClickListener {
 
	private ListView lstnoti;
	private LinearLayout emptydata;
	private AdapterNotificacion adapter;
	private ProgressDialog progressDialog;
	private	PullToRefreshLayout mPullToRefreshLayout;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.activity_notifications, container, false);
         
        lstnoti = (ListView) rootView.findViewById(R.id.listnoitificaciones);
        emptydata = (LinearLayout) rootView.findViewById(R.id.empty_data);
        emptydata.setVisibility(View.GONE);	
        lstnoti.setOnItemClickListener(this);        
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
                .theseChildrenArePullable(R.id.listnoitificaciones, android.R.id.empty)
                .listener(this)
                .setup(mPullToRefreshLayout);
        
    }
    
    
    @Override
    public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	getValues(getString(R.string.url_notificaciones));
    }
    
    
    private void getValues(final String url){		

			progressDialog = new ProgressDialog(getActivity());
	    	progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    	progressDialog.setMessage("Loading...");
	    	progressDialog.setCancelable(false);
	    	progressDialog.show();
	    
			Thread tr = new Thread(new Runnable() {			
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpSisno registra =  new HttpSisno(getActivity().getApplicationContext(),url);
					ArrayList<Notificacion> lst = registra.listarNotificaciones(parametros());					
					AdapterNotificacion adapter = new AdapterNotificacion(getActivity(), lst);
					
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
			lstnoti.setAdapter(adapter);
			if(adapter.getCount()!=0){
				emptydata.setVisibility(View.GONE);	
			}else{
				emptydata.setVisibility(View.VISIBLE);
			}
			mPullToRefreshLayout.setRefreshComplete();
		};		
	};
	
    
    private String parametros(){
		
		ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();		
        values.add(new BasicNameValuePair(getString(R.string.faccion),"listarnottodas"));	
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
		Toast.makeText(getActivity(), noti.getTitulo(), Toast.LENGTH_LONG).show();
		Intent intent = new Intent(getActivity(), DetalleNotificacion.class);
		intent.putExtra("code", String.valueOf(noti.getCodigo()));
		intent.putExtra("title", noti.getTitulo());
		intent.putExtra("descripcion", noti.getDescripcion());
		intent.putExtra("imagen", noti.getImagen());
		intent.putExtra("megusta", String.valueOf(noti.getMegusta()));
		startActivity(intent);
		
	}


	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		getValues(getString(R.string.url_notificaciones));
		
		
	}
}