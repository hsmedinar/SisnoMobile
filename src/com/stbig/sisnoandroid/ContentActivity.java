package com.stbig.sisnoandroid;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.stbig.sisnoandroid.data.FragmentPageAdapter;
import com.stbig.sisnoandroid.services.checkServices;
import com.viewpagerindicator.UnderlinePageIndicator;

public class ContentActivity extends FragmentActivity implements ActionBar.TabListener {

	FragmentPageAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;	
	ActionBar actionBar;
	Intent serviceUbicacion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		settitle(0);
		
		mSectionsPagerAdapter = new FragmentPageAdapter(getSupportFragmentManager(),this);
		mViewPager = (ViewPager) findViewById(R.id.pager);		
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setPageMargin(10);
		mViewPager.setPageMarginDrawable(R.color.black);
		
		UnderlinePageIndicator mIndicator = (UnderlinePageIndicator)findViewById(R.id.indicator);
		mIndicator.setFades(false);
        mIndicator.setViewPager(mViewPager);
        
        
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.icon_title_notificacion).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.icon_title_tienda).setTabListener(this));        
        
        mIndicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
        	@Override
        	public void onPageSelected(int position) {
        		// TODO Auto-generated method stub
        		super.onPageSelected(position);
        		
        		actionBar.setSelectedNavigationItem(position);
        		settitle(position);
        	}        	
        });		 
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		serviceUbicacion = new Intent(ContentActivity.this, checkServices.class);
		startService(serviceUbicacion);
		Log.i("onresumen", "content resumen");
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(tab.getPosition());
		settitle(tab.getPosition());
		switch(tab.getPosition()){
			case 0 : tab.setIcon(R.drawable.icon_title_notificacion);
					break;
			case 1 : tab.setIcon(R.drawable.icon_title_tienda);
					break;
		}		
	}
	
	private void settitle(int position){
		switch(position){
		case 0 : actionBar.setTitle(R.string.title_notifications);;
				break;
		case 1 : actionBar.setTitle(R.string.title_stores);
				break;
		}				
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
		switch(tab.getPosition()){
			case 0 : tab.setIcon(R.drawable.icon_title_notificacion_disabled);
					 break;
			case 1 : tab.setIcon(R.drawable.icon_title_tienda_disabled);
					 break;
		}
	}
}
