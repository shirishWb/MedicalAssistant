package com.whitebirdtechnology.medicalassistant.LogInScreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whitebirdtechnology.medicalassistant.HomeScreen.MainActivityHomeScreen;
import com.whitebirdtechnology.medicalassistant.R;
import com.whitebirdtechnology.medicalassistant.Server.BackgroundTask;
import com.whitebirdtechnology.medicalassistant.Server.ServerResponse;
import com.whitebirdtechnology.medicalassistant.Sharepreference.ClsSharePreference;
import com.whitebirdtechnology.medicalassistant.SignUpScreen.MainActivitySignUp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivityLogInScreen extends AppCompatActivity implements View.OnClickListener,ServerResponse{
    EditText editTextEmail,editTextPassword;
    TextView textViewLogin,textViewSignUp,textViewFbLogin;
    String stringPass,stringEmail;
    ClsSharePreference clsSharePreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_log_in_screen);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        textViewFbLogin = (TextView)findViewById(R.id.textViewFacebookLogin);
        textViewLogin = (TextView)findViewById(R.id.textViewLogIn);
        textViewSignUp = (TextView)findViewById(R.id.textViewSignUp);
        clsSharePreference = new ClsSharePreference(this);
        textViewSignUp.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
        textViewFbLogin.setOnClickListener(this);

    }
    boolean isEmailValid() {
        stringEmail = editTextEmail.getText().toString();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(stringEmail).matches();
    }
    @Override
    public void onClick(View v) {

        if(v==textViewLogin){
           // startActivity(new Intent(MainActivityLogInScreen.this, MainActivityHomeScreen.class));
            if(!isEmailValid()) {
                Toast.makeText(this, "Please Enter Valid Email Id.", Toast.LENGTH_SHORT).show();
                return;
            }
            stringPass = editTextPassword.getText().toString();
            if(stringPass.isEmpty()){
                Toast.makeText(this, "Please Enter Pass.", Toast.LENGTH_SHORT).show();
                return;
            }
            HashMap<String,String> params = new HashMap<>();
            params.put(getString(R.string.serviceKeyEmail),stringEmail);
            params.put(getString(R.string.serviceKeyPassword),stringPass);
            new BackgroundTask(this,params,getString(R.string.LoginURL)).execute();

        }
        if(v==textViewSignUp){
            startActivity(new Intent(MainActivityLogInScreen.this, MainActivitySignUp.class));
        }
    }

    @Override
    public void Response(String result, String methodKey) {
        try {
            JSONObject object = new JSONObject(result);
            String success = object.getString(getString(R.string.serviceKeySuccess));
            if(success.equals(getString(R.string.serviceKeySuccessValue))){
                String profile = object.getString(getString(R.string.serviceKeyProfData));
                JSONObject object1 = new JSONObject(profile);
                clsSharePreference.SetSharePref(getString(R.string.SharPrfUID),object1.getString(getString(R.string.serviceKeyUID)));
                clsSharePreference.SetSharePref(getString(R.string.SharPrfEmail),object1.getString(getString(R.string.serviceKeyEmail)));
                clsSharePreference.SetSharePref(getString(R.string.SharPrfName),object1.getString(getString(R.string.serviceKeyName)));
                clsSharePreference.SetSharePref(getString(R.string.SharPrfMobileNo),object1.getString(getString(R.string.serviceKeyMobileNo)));
                Toast.makeText(this, object.getString(getString(R.string.serviceKeyMsg)), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivityLogInScreen.this, MainActivityHomeScreen.class));
            }else {
                Toast.makeText(this, object.getString(getString(R.string.serviceKeyMsg)), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
