package com.whitebirdtechnology.medicalassistant.Sharepreference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dell on 18/3/17.
 */

public class ClsSharePreference {
    SharedPreferences sharedPreferences;
    public ClsSharePreference(Activity activity){
        sharedPreferences = activity.getSharedPreferences("MedicalAssistanceSharePrf", Context.MODE_PRIVATE);
    }
    public void SetSharePref(String key,String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public String GetSharPrf(String key){
       return sharedPreferences.getString(key,"");
    }
}
