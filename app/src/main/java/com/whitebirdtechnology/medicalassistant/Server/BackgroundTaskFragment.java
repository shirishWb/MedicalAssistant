package com.whitebirdtechnology.medicalassistant.Server;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import java.util.HashMap;

/**
 * Created by dell on 23/3/17.
 */

public class BackgroundTaskFragment extends AsyncTask<Void,Void,Void> {
    String methodName;
    VolleyServerClass volleyServerClass;
    HashMap<String,String> param;
    Fragment fragment;
    public BackgroundTaskFragment(Fragment fragment, HashMap<String,String> param,String methodName){
        this.param =param;
        this.methodName = methodName;
        this.fragment =fragment;

        volleyServerClass = new VolleyServerClass(fragment);
    }
    @Override
    protected Void doInBackground(Void... params) {

        volleyServerClass.getDataFromUrl(methodName,param);
        return null;
    }
}
