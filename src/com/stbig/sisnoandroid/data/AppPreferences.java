package com.stbig.sisnoandroid.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class AppPreferences {

    //private String namePrefernces="ftpConfig";
    private SharedPreferences sharePrefences;
    private Editor editor;

    public AppPreferences(Context context) {
        this.sharePrefences = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = sharePrefences.edit();
    }

    public String getValue(String key) {
        return sharePrefences.getString(key, "");
    }    

    public boolean getValueBoolean(String key) {
        return sharePrefences.getBoolean(key, false);
    }

    public void saveValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }
    
    public void saveValueBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

}
