package com.whitebirdtechnology.medicalassistant.Server;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ClearCacheRequest;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whitebirdtechnology.medicalassistant.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 18/3/17.
 */

public class VolleyServerClass {
    private static String stringURL = null;
    private static int timeout = 60000;
    Activity activity;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    Fragment fragment;
    public VolleyServerClass(Activity activity){
        this.activity = activity;
        if(this.activity==null)
            return;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
            }
        });
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(activity);
        stringURL = activity.getString(R.string.serverURL);

    }
    public VolleyServerClass(Fragment fragment){
        activity = fragment.getActivity();
        if(activity==null)
            return;
        this.fragment = fragment;
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(fragment.getActivity());
        stringURL = fragment.getActivity().getString(R.string.serverURL);

    }

    public void getDataFromUrl(final String method_name, final HashMap<String, String> params) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, stringURL + method_name, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ServerResponse serverResponse = null;
                serverResponse = (ServerResponse) fragment;
                serverResponse.Response(response,method_name);
                requestQueue.getCache().clear();
                params.clear();
                new DiskBasedCache(activity.getCacheDir()).clear();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  DissmissDialog(activity);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return params;
            }
        };requestQueue.add(stringRequest);
    }

    public void getDataFromUrl(final HashMap<String,String> params, final String METHOD_NAME){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, stringURL + METHOD_NAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ServerResponse serverResponse = null;
                serverResponse = (ServerResponse) activity;
                serverResponse.Response(response,METHOD_NAME);
                requestQueue.getCache().clear();
                params.clear();
                new DiskBasedCache(activity.getCacheDir()).clear();
                DissmissDialog(activity);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DissmissDialog(activity);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return params;
            }
        };requestQueue.add(stringRequest);
    }

    public void DissmissDialog(Activity activity){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }
}
