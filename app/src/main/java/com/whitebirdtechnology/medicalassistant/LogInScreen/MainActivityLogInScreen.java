package com.whitebirdtechnology.medicalassistant.LogInScreen;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
    ActionBar.LayoutParams params;
    TextView Title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.action_bar_layout, null);
        params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        Title = (TextView) view.findViewById(R.id.actionbar_title);
        Title.setText("Log In");

        getSupportActionBar().setCustomView(view,params);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
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
            params.put(getString(R.string.serviceKeyFlagCate),clsSharePreference.GetSharPrf(getString(R.string.SharPrfUserType)));
            new BackgroundTask(this,params,getString(R.string.LoginURL)).execute();
            editTextPassword.setText("");
            editTextEmail.setText("");
            stringEmail ="";
            stringPass= "";
        }
        if(v==textViewSignUp){
            startActivity(new Intent(MainActivityLogInScreen.this, MainActivitySignUp.class));
        }
    }

    @Override
    public void Response(String result, String methodKey) {
        Log.d("resultLogin",result);
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
                clsSharePreference.SetSharePref(getString(R.string.SharPrfProImg),object1.getString(getString(R.string.serviceKeyImageProf)));
                Toast.makeText(this, object.getString(getString(R.string.serviceKeyMsg)), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivityLogInScreen.this, MainActivityHomeScreen.class));
            }else {
                Toast.makeText(this, object.getString(getString(R.string.serviceKeyMsg)), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
