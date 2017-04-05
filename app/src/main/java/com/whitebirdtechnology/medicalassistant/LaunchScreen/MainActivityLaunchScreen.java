package com.whitebirdtechnology.medicalassistant.LaunchScreen;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.whitebirdtechnology.medicalassistant.LogInScreen.MainActivityLogInScreen;
import com.whitebirdtechnology.medicalassistant.R;
import com.whitebirdtechnology.medicalassistant.Sharepreference.ClsSharePreference;

public class MainActivityLaunchScreen extends AppCompatActivity implements View.OnClickListener {
    TextView textViewUserLogin;
    TextView textViewExpertLogin;
    ClsSharePreference clsSharePreference;
    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_launch_screen);
        clsSharePreference = new ClsSharePreference(this);
        textViewExpertLogin = (TextView)findViewById(R.id.textViewExpertLogin);
        textViewUserLogin =(TextView)findViewById(R.id.textViewUserLogin);
        textViewExpertLogin.setOnClickListener(this);
        textViewUserLogin.setOnClickListener(this);
        verifyStoragePermissions(this);
    }

    @Override
    public void onClick(View v) {
        if(v==textViewExpertLogin){
            clsSharePreference.SetSharePref(getString(R.string.SharPrfUserType),getString(R.string.expertType));
            startActivity(new Intent(MainActivityLaunchScreen.this, MainActivityLogInScreen.class));
        }
        if(v==textViewUserLogin){
            clsSharePreference.SetSharePref(getString(R.string.SharPrfUserType),"2");
            startActivity(new Intent(MainActivityLaunchScreen.this, MainActivityLogInScreen.class));
        }

    }

}

