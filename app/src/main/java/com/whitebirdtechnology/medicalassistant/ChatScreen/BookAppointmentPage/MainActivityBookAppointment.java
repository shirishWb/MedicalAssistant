package com.whitebirdtechnology.medicalassistant.ChatScreen.BookAppointmentPage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitebirdtechnology.medicalassistant.ChatScreen.BookAppointmentPage.AppointmentTime.MainActivitySetTime;
import com.whitebirdtechnology.medicalassistant.R;
import com.whitebirdtechnology.medicalassistant.Server.BackgroundTask;
import com.whitebirdtechnology.medicalassistant.Server.ServerResponse;
import com.whitebirdtechnology.medicalassistant.SignUpScreen.MainActivityOTP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivityBookAppointment extends AppCompatActivity implements View.OnClickListener,ServerResponse, CalendarView.OnDateChangeListener{
    CalendarView calendarView;
    Calendar calendar;
    ActionBar.LayoutParams params;
    TextView Title;
    Bundle bundle;
    String stringName,stringOcupation,stringExpertId,stringImgProf;
    TextView textViewName,textViewOccupation;
    ImageView imageViewProf;
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
        setContentView(R.layout.activity_main_book_appointment);
        textViewName = (TextView)findViewById(R.id.textViewNameBook);
        textViewOccupation = (TextView)findViewById(R.id.textViewOccupationBook);
        imageViewProf = (ImageView)findViewById(R.id.imageViewProfBook);

        bundle = getIntent().getBundleExtra("BundleEx");
        textViewName.setText(bundle.getString("EName"));
        textViewOccupation.setText(bundle.getString("EOccupation"));
        byte[] decodedString = Base64.decode(bundle.getString("EImg"), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageViewProf.setImageBitmap(decodedByte);
        stringImgProf = bundle.getString("EImg");
        stringExpertId = bundle.getString("EId");

        /*calendarView.setOnClickListener(this);*/
        calendarView = (CalendarView)findViewById(R.id.calenderView);
        calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),1);
        Long date = System.currentTimeMillis();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,2);
        Long dateEnd = calendar.getTime().getTime();
        calendarView.setMinDate(date);
        calendarView.setOnDateChangeListener(this);
        calendarView.setMaxDate(dateEnd);

    }

    @Override
    public void onClick(View v) {
        /*if(v==calendarView){
            HashMap<String,String> para = new HashMap<>();
            para.put(getString(R.string.serviceKeyExpertId),stringExpertId);
            para.put(getString(R.string.serviceKeyDate), String.valueOf(calendarView.getDate()));
            new BackgroundTask(this,para,getString(R.string.appointmentDate)).execute();

        }*/
    }

    @Override
    public void Response(String result, String methodKey) {
        Log.d("calendarDate",result);
        try {
            JSONObject object = new JSONObject(result);
            String success = object.getString(getString(R.string.serviceKeySuccess));
            String message = object.getString(getString(R.string.serviceKeyMsg));
            if(success.equals(getString(R.string.serviceKeySuccessValue))){
                ArrayList<String> arrayList = new ArrayList<>();
                JSONArray jsonArray = object.getJSONArray(getString(R.string.serviceKeyTimeArray));
                for(int i =0;i<jsonArray.length();i++){
                    JSONObject object1 = jsonArray.getJSONObject(i);
                    arrayList.add(object1.getString(getString(R.string.serviceKeyTime)));
                }
                Intent intent = new Intent(MainActivityBookAppointment.this, MainActivityOTP.class);
                bundle.putStringArrayList("TimeList",arrayList);
                intent.putExtra("BundleExtra",bundle);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String weekDay = null;
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date now = simpleDateformat.parse(year+"-"+month+"-"+dayOfMonth);
            calendar.setTimeInMillis(now.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek ) {
            case Calendar.SUNDAY:
                weekDay = "SUNDAY";
                    break;
            case Calendar.MONDAY:
                weekDay = "MONDAY";
                    break;
            case Calendar.TUESDAY:
                weekDay = "TUESDAY";
                break;
            case Calendar.WEDNESDAY:
                weekDay = "WEDNESDAY";
                break;
            case Calendar.THURSDAY:
                weekDay = "THURSDAY";
                break;
            case Calendar.FRIDAY:
                weekDay = "FRIDAY";
                break;
            case Calendar.SATURDAY:
                weekDay = "SATURDAY";
                break;
        }
        Intent intent = new Intent(this, MainActivitySetTime.class);
        intent.putExtra("Date",weekDay+" "+dayOfMonth+ " "+monthNames[month]+" "+ year);
        intent.putExtra("BundleEx",bundle);
        startActivity(intent);
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
