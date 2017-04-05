package com.whitebirdtechnology.medicalassistant.ChatScreen.FullScreenImageChat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitebirdtechnology.medicalassistant.R;

public class MainActivityFullScreenImgChat extends AppCompatActivity implements View.OnClickListener {
    ImageView imageViewFull;
    ActionBar.LayoutParams params;
    TextView Title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.action_bar_layout, null);
        params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        Title = (TextView) view.findViewById(R.id.actionbar_title);
        Title.setText("Gallery");
        getSupportActionBar().setCustomView(view,params);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();*/
        setContentView(R.layout.activity_main_full_screen_img_chat);
        imageViewFull = (ImageView)findViewById(R.id.imageViewImgFullScreen);
        byte[] bytes = getIntent().getByteArrayExtra("ImgByteArray");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageViewFull.setImageBitmap(bitmap);
        imageViewFull.setOnClickListener(this);
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
    public void onClick(View v) {
        if(v==imageViewFull){

        }
    }
    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                View decorView = getWindow().getDecorView();
                // Hide the status bar.
                /*int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                decorView.setSystemUiVisibility(uiOptions);*/
                // Remember that you should never show the action bar if the
                // status bar is hidden, so hide that too if necessary.
                if(getSupportActionBar().isShowing()){
                    getSupportActionBar().hide();
                }else
                    getSupportActionBar().show();
            }
        }, 1000);
    }
}
