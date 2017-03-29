package com.whitebirdtechnology.medicalassistant.SignUpScreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.whitebirdtechnology.medicalassistant.LogInScreen.MainActivityLogInScreen;
import com.whitebirdtechnology.medicalassistant.R;
import com.whitebirdtechnology.medicalassistant.Server.BackgroundTask;
import com.whitebirdtechnology.medicalassistant.Server.ServerResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivityOTP extends AppCompatActivity implements View.OnClickListener,ServerResponse {
    TextView textViewSignUp,textViewNext,textViewOTPAgain,textViewTryAgain,textViewMobileNo;
    ImageView imageViewProf;
    HashMap<String,String> params;
    ActionBar.LayoutParams param;
    TextView Title;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.action_bar_layout, null);
        param = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        Title = (TextView) view.findViewById(R.id.actionbar_title);
        Title.setText("Sign Up");

        getSupportActionBar().setCustomView(view,param);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_main_otp);
        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        params =(HashMap<String, String>) bundle.getSerializable("HashMapParams");
        // Intent intent = new Intent();
        textViewSignUp = (TextView)findViewById(R.id.textViewSignUp);
        imageViewProf = (ImageView)findViewById(R.id.imageViewProf);
        textViewNext = (TextView)findViewById(R.id.textViewNext);
        textViewOTPAgain = (TextView)findViewById(R.id.textViewGetOTPAgain);
        textViewTryAgain = (TextView)findViewById(R.id.textViewTryAgain);
        textViewMobileNo = (TextView)findViewById(R.id.textViewMobNo);
        String text = "Enter One Time Password(OTP) we just send to ";
        String mob  =params.get(getString(R.string.serviceKeyMobileNo));
        textViewMobileNo.setText(text+mob);
        byte[] decodedString = Base64.decode(params.get(getString(R.string.serviceKeyImageProf)), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageViewProf.setImageBitmap(decodedByte);
        textViewSignUp.setOnClickListener(this);
        textViewOTPAgain.setOnClickListener(this);
        textViewNext.setOnClickListener(this);
        textViewTryAgain.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v==textViewSignUp){
            new BackgroundTask(this,params,getString(R.string.SignUpURL)).execute();
        }
        if(v==textViewTryAgain){
            finish();
        }
    }

    @Override
    public void Response(String result, String methodKey) {
        try {
            Log.d("Signup",result);
            JSONObject object = new JSONObject(result);
            String success = object.getString(getString(R.string.serviceKeySuccess));
            if(success.equals(getString(R.string.serviceKeySuccessValue))){
                Toast.makeText(this, object.getString("message"), Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivityOTP.this,MainActivityLogInScreen.class));
                setResult(RESULT_OK);
                MainActivityOTP.this.finish();


            }else {
                Toast.makeText(this, object.getString("message"), Toast.LENGTH_SHORT).show();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
