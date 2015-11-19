package com.stbig.sisnoandroid.data;

import java.util.Locale;

import com.stbig.sisnoandroid.R;
import com.stbig.sisnoandroid.fragments.FragmentNotifications;
import com.stbig.sisnoandroid.fragments.FragmentTiendas;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class FragmentPageAdapter extends FragmentStatePagerAdapter {

	private Context context;
	
	public FragmentPageAdapter(FragmentManager fm, Context c) {
		super(fm);
		context = c;
	}

	@Override
	public Fragment getItem(int position) {
		
		switch(position){
		case 0:
			return new FragmentNotifications();
			
		case 1:
			return new FragmentTiendas();
		}
		
		return 	null;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return context.getString(R.string.title_notifications).toUpperCase(l);
		case 1:
			return context.getString(R.string.title_stores).toUpperCase(l);
		}
		
		return null;
	}
}
