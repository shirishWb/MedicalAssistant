package com.whitebirdtechnology.medicalassistant.ChatScreen.BookAppointmentPage.AppointmentTime;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitebirdtechnology.medicalassistant.R;

import java.util.ArrayList;

public class MainActivitySetTime extends AppCompatActivity {
    ActionBar.LayoutParams params;
    TextView Title;
    Bundle bundle;
    String stringName,stringOcupation,stringExpertId,stringImgProf;
    TextView textViewName,textViewOccupation,textViewDate;
    ImageView imageViewProf;
    ArrayList<String> arrayListTime;
    GridTimeAdapter adapter;
    GridView gridViewTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.action_bar_layout, null);
        params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        Title = (TextView) view.findViewById(R.id.actionbar_title);
        Title.setText("Book Appointment");

        getSupportActionBar().setCustomView(view,params);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_main_set_time);
        textViewName = (TextView)findViewById(R.id.textViewNameBook);
        textViewOccupation = (TextView)findViewById(R.id.textViewOccupationBook);
        imageViewProf = (ImageView)findViewById(R.id.imageViewProfBook);
        textViewDate = (TextView)findViewById(R.id.textViewDateShown);
        gridViewTime = (GridView)findViewById(R.id.gridViewTime);
        textViewDate.setText(getIntent().getStringExtra("Date"));
        bundle = getIntent().getBundleExtra("BundleEx");
        textViewName.setText(bundle.getString("EName"));
        textViewOccupation.setText(bundle.getString("EOccupation"));
        byte[] decodedString = Base64.decode(bundle.getString("EImg"), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageViewProf.setImageBitmap(decodedByte);
        stringImgProf = bundle.getString("EImg");
        stringExpertId = bundle.getString("EId");
        arrayListTime = new ArrayList<>();
        arrayListTime.add("10.00");
        arrayListTime.add("10.20");
        arrayListTime.add("10.40");
        arrayListTime.add("11.00");
        arrayListTime.add("11.20");
        arrayListTime.add("11.40");
        arrayListTime.add("13.00");
        arrayListTime.add("13.20");
        arrayListTime.add("13.40");
        arrayListTime.add("15.00");
        arrayListTime.add("15.30");
        arrayListTime.add("16.00");

        adapter = new GridTimeAdapter(this,arrayListTime);
        gridViewTime.setAdapter(adapter);

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
