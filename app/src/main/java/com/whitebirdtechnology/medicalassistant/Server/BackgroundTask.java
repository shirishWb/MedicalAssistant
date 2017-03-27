package com.whitebirdtechnology.medicalassistant.Server;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.HashMap;

/**
 * Created by dell on 7/3/17.
 */

public class BackgroundTask extends AsyncTask<Void,Void,Void> {
    String methodName;
    Activity activity;
    VolleyServerClass volleyServerClass;
    HashMap<String,String> param;

   public BackgroundTask(Activity activity, HashMap<String,String> params, String MethodName){
       this.activity = activity;
       this.param =params;
       methodName = MethodName;
       volleyServerClass = new VolleyServerClass(activity);

   }



    @Override
    protected Void doInBackground(Void... params) {
        volleyServerClass.getDataFromUrl(param,methodName);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }


}
