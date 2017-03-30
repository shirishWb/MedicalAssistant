package com.whitebirdtechnology.medicalassistant.ChatScreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.whitebirdtechnology.medicalassistant.ChatScreen.BookAppointmentPage.MainActivityBookAppointment;
import com.whitebirdtechnology.medicalassistant.LaunchScreen.MainActivityLaunchScreen;
import com.whitebirdtechnology.medicalassistant.R;
import com.whitebirdtechnology.medicalassistant.Server.BackgroundTask;
import com.whitebirdtechnology.medicalassistant.Server.ServerResponse;
import com.whitebirdtechnology.medicalassistant.Sharepreference.ClsSharePreference;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivityChat extends AppCompatActivity implements View.OnClickListener,ServerResponse {
    TextView textViewName,textViewOccupation,textViewIsTyping;
    ImageView imageViewProf;
    ImageButton imageButtonCall,imageButtonAttach,imageButtonOption,imageButtonAdd,imageButtonSend;
    Bundle bundle;
    String stringName,stringOcupation,stringExpertId,stringImgProf;
    Boolean aBooleanIsFavourite;
    ListView listViewChat;
    ActionBar.LayoutParams params;
    TextView Title;
    ArrayList<FeedItemChat> arrayListChat;
    ChatAdapter chatAdapter;
    EditText editTextMsg;
    ClsSharePreference clsSharePreference;
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
        setContentView(R.layout.activity_main_chat);
        textViewIsTyping = (TextView)findViewById(R.id.textViewIsTyping);
        textViewName = (TextView)findViewById(R.id.textViewName);
        textViewOccupation = (TextView)findViewById(R.id.textViewOccupation);
        imageViewProf = (ImageView)findViewById(R.id.imageViewProf);
        imageButtonAdd =(ImageButton)findViewById(R.id.imageButtonAdd);
        imageButtonAttach=(ImageButton)findViewById(R.id.imageButtonAttach);
        imageButtonSend=(ImageButton)findViewById(R.id.imageButtonSend);
        imageButtonCall=(ImageButton)findViewById(R.id.imageButtonCall);
        imageButtonOption=(ImageButton)findViewById(R.id.imageButtonOption);
        listViewChat = (ListView)findViewById(R.id.listViewChat);
        editTextMsg = (EditText)findViewById(R.id.editTextChatMsg);
        imageButtonOption.setOnClickListener(this);
        imageButtonAttach.setOnClickListener(this);
        imageButtonAdd.setOnClickListener(this);
        imageButtonCall.setOnClickListener(this);
        imageButtonSend.setOnClickListener(this);
        clsSharePreference = new ClsSharePreference(this);
        bundle = getIntent().getBundleExtra("BundleExpert");
        textViewName.setText(bundle.getString("EName"));
        textViewOccupation.setText(bundle.getString("EOccupation"));
        byte[] decodedString = Base64.decode(bundle.getString("EImg"), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageViewProf.setImageBitmap(decodedByte);
        stringImgProf = bundle.getString("EImg");
        stringExpertId = bundle.getString("EId");
        aBooleanIsFavourite = bundle.getBoolean("BoolFav");
        arrayListChat = new ArrayList<>();
        chatAdapter = new ChatAdapter(this,arrayListChat);
        listViewChat.setAdapter(chatAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v==imageButtonOption){
            PopupMenu popupMenu = new PopupMenu(this,v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_option,popupMenu.getMenu());
            MenuItem item = popupMenu.getMenu().findItem(R.id.menuItemAddFav);
            if(aBooleanIsFavourite){
                item.setTitle("Remove Favourite");
            }else {
                item.setTitle("Add Favourite");
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if(id==R.id.menuItemBook){
                        Intent intent = new Intent(MainActivityChat.this, MainActivityBookAppointment.class);
                        intent.putExtra("BundleEx",bundle);
                    startActivity(intent);
                    }else if(id==R.id.menuItemAddFav){
                        if(aBooleanIsFavourite){
                            HashMap<String,String> params = new HashMap<String, String>();
                            params.put(getString(R.string.serviceKeyExpertId),stringExpertId);
                            params.put(getString(R.string.serviceKeyUID),clsSharePreference.GetSharPrf(getString(R.string.SharPrfUID)));
                            params.put(getString(R.string.serviceKeyFlagCate),"1");
                            new BackgroundTask(MainActivityChat.this,params,getString(R.string.favouriteURL)).execute();
                            aBooleanIsFavourite =false;
                            item.setTitle("Add Favourite");
                        }else {
                            HashMap<String,String> params = new HashMap<String, String>();
                            params.put(getString(R.string.serviceKeyExpertId),stringExpertId);
                            params.put(getString(R.string.serviceKeyUID),clsSharePreference.GetSharPrf(getString(R.string.SharPrfUID)));
                            params.put(getString(R.string.serviceKeyFlagCate),"1");
                            new BackgroundTask(MainActivityChat.this,params,getString(R.string.favouriteURL)).execute();
                            aBooleanIsFavourite =true;
                            item.setTitle("Remove Favourite");
                        }
                    }
                    return true;
                }
            });
            popupMenu.show();
        }
        if(v==imageButtonSend){
            if(!editTextMsg.getText().toString().isEmpty()){
                FeedItemChat feedItemChat = new FeedItemChat();
                feedItemChat.setStringFlag("1");
                //feedItemChat.setStringImgPath(clsSharePreference.GetSharPrf(getString(R.string.SharPrfProImg)));
                feedItemChat.setStringMsg(editTextMsg.getText().toString());
                feedItemChat.setStringImgPath(stringImgProf);
                editTextMsg.setText("");
                Date dt = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                String time = sdf.format(dt);
                feedItemChat.setStringTime(time);
                arrayListChat.add(feedItemChat);
                chatAdapter.notifyDataSetChanged();
               //
            }
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
    public void Response(String result, String methodKey) {
        Log.d("ResultIsLike",result);
    }
}
