package com.whitebirdtechnology.medicalassistant.LaunchScreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.whitebirdtechnology.medicalassistant.LogInScreen.MainActivityLogInScreen;
import com.whitebirdtechnology.medicalassistant.R;

public class MainActivityLaunchScreen extends AppCompatActivity implements View.OnClickListener {
    TextView textViewUserLogin;
    TextView textViewExpertLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_launch_screen);
        textViewExpertLogin = (TextView)findViewById(R.id.textViewExpertLogin);
        textViewUserLogin =(TextView)findViewById(R.id.textViewUserLogin);
        textViewExpertLogin.setOnClickListener(this);
        textViewUserLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==textViewExpertLogin){
            startActivity(new Intent(MainActivityLaunchScreen.this, MainActivityLogInScreen.class));
        }

    }
}

