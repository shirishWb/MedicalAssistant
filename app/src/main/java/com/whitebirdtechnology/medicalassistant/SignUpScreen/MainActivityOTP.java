package com.whitebirdtechnology.medicalassistant.SignUpScreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
    TextView textViewSignUp,textViewNext,textViewOTPAgain,textViewTryAgain;
    ImageView imageViewProf;
    HashMap<String,String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_otp);
        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        params =(HashMap<String, String>) bundle.getSerializable("HashMapParams");
        // Intent intent = new Intent();
        textViewSignUp = (TextView)findViewById(R.id.textViewSignUp);
        imageViewProf = (ImageView)findViewById(R.id.imageViewProf);
        textViewNext = (TextView)findViewById(R.id.textViewNext);
        textViewOTPAgain = (TextView)findViewById(R.id.textViewGetOTPAgain);
        textViewTryAgain = (TextView)findViewById(R.id.textViewTryAgain);
        byte[] decodedString = Base64.decode(params.get(getString(R.string.serviceKeyImageProf)), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageViewProf.setImageBitmap(decodedByte);
        textViewSignUp.setOnClickListener(this);
        textViewOTPAgain.setOnClickListener(this);
        textViewNext.setOnClickListener(this);
        textViewTryAgain.setOnClickListener(this);
       // intent.putExtra("bundlePass",bundle);
        //setResult(4,intent);



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
                startActivity(new Intent(MainActivityOTP.this,MainActivityLogInScreen.class));
                MainActivityOTP.this.finish();

            }else {
                Toast.makeText(this, object.getString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
