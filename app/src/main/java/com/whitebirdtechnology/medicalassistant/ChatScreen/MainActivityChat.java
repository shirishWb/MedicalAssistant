package com.whitebirdtechnology.medicalassistant.ChatScreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.whitebirdtechnology.medicalassistant.ChatMsgFirebaseSync;
import com.whitebirdtechnology.medicalassistant.ChatScreen.BookAppointmentPage.MainActivityBookAppointment;
import com.whitebirdtechnology.medicalassistant.FeedItemUserInfo;
import com.whitebirdtechnology.medicalassistant.R;
import com.whitebirdtechnology.medicalassistant.Server.BackgroundTask;
import com.whitebirdtechnology.medicalassistant.Server.ServerResponse;
import com.whitebirdtechnology.medicalassistant.Sharepreference.ClsSharePreference;
import com.whitebirdtechnology.medicalassistant.SqlDatabase.SqlDatabaseChat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MainActivityChat extends AppCompatActivity implements View.OnClickListener,ServerResponse,RefreshAdapterInterface {
    TextView textViewName,textViewOccupation,textViewIsTyping;
    ImageView imageViewProf;
    ImageButton imageButtonCall,imageButtonAttach,imageButtonOption,imageButtonAdd,imageButtonSend;
    Bundle bundle;
    String stringName,stringOcupation,stringExpertId,stringImgProf,stringExpertMobNo;
    Boolean aBooleanIsFavourite;
    ListView listViewChat;
    ActionBar.LayoutParams params;
    TextView Title;
    ArrayList<FeedItemChat> arrayListChat;
    ChatAdapter chatAdapter;
    EditText editTextMsg;
    ClsSharePreference clsSharePreference;
    int height,width;
    long expertMobNo, userMobNo;
    public static  DatabaseReference database ;
    String uniqueNo;
    SqlDatabaseChat sqlDatabaseChat;
    ChatMsgFirebaseSync chatMsgFirebaseSync;
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
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.light_blue_chat));
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.light_blue_chat));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_blue_chat)));
        getSupportActionBar().setCustomView(view,params);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_main_chat);
        clsSharePreference = new ClsSharePreference(this);
        chatMsgFirebaseSync = new ChatMsgFirebaseSync(this);
        chatMsgFirebaseSync.AddDataToSqlFrmFirebase();
        sqlDatabaseChat = new SqlDatabaseChat(this);
        bundle = getIntent().getBundleExtra("BundleExpert");
        stringImgProf = bundle.getString("EImg");
        stringExpertId = bundle.getString("EId");
        aBooleanIsFavourite = bundle.getBoolean("BoolFav");
        stringExpertMobNo = bundle.getString("EMobNo");
        expertMobNo = Long.parseLong(stringExpertMobNo);
        userMobNo = Long.parseLong(clsSharePreference.GetSharPrf(getString(R.string.SharPrfMobileNo)));

        if(expertMobNo > userMobNo){
            uniqueNo = String.valueOf(userMobNo)+String.valueOf(expertMobNo);
        }else {
            uniqueNo = String.valueOf(expertMobNo)+String.valueOf(userMobNo);
        }
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
        textViewName.setText(bundle.getString("EName"));
        textViewOccupation.setText(bundle.getString("EOccupation"));
        byte[] decodedString = Base64.decode(bundle.getString("EImg"), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageViewProf.setImageBitmap(decodedByte);
        /*FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainImagesRef = storageRef.child("ProfileImages/"+"UserId"+expertMobNo+"/"+"Profile.jpg");
        UploadTask uploadTask = mountainImagesRef.putBytes(decodedString);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("image upload",exception.getMessage());
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();*/
                DatabaseReference database1 = FirebaseDatabase.getInstance().getReference();
                DatabaseReference refInfo = database1.child("UserInfo");
                DatabaseReference userId = refInfo.child(clsSharePreference.GetSharPrf(getString(R.string.SharPrfMobileNo)));
                DatabaseReference info = userId.child(stringExpertMobNo);
                HashMap<String,String> exrpertInfo = new HashMap<>();
                exrpertInfo.put("Name",bundle.getString("EName"));
                exrpertInfo.put("Occupation",bundle.getString("EOccupation"));
                exrpertInfo.put("IsFavourite", String.valueOf(aBooleanIsFavourite));
                exrpertInfo.put("UserId",stringExpertId);
                exrpertInfo.put("ProfilePath",stringImgProf);
                info.setValue(exrpertInfo);
                FeedItemUserInfo feedItemUserInfo = new FeedItemUserInfo();
                feedItemUserInfo.setStringMobileNo(stringExpertMobNo);
                feedItemUserInfo.setStringId(clsSharePreference.GetSharPrf(getString(R.string.SharPrfUID)));
                feedItemUserInfo.setStringImgPath(stringImgProf);
                feedItemUserInfo.setStringIsFav(String.valueOf(false));
                feedItemUserInfo.setStringName(bundle.getString("EName"));
                feedItemUserInfo.setStringOccu(bundle.getString("EOccupation"));
                sqlDatabaseChat.AddUserInfo(feedItemUserInfo);
           /* }
        });*/


        final int[] size = {0};
        arrayListChat = sqlDatabaseChat.GetChatMsg("TABLE"+String.valueOf(uniqueNo));
        chatAdapter = new ChatAdapter(MainActivityChat.this, arrayListChat);
        listViewChat.setAdapter(chatAdapter);

/*
                                      database = FirebaseDatabase.getInstance().getReference();
                                      DatabaseReference ref = database.child("ChatMsg");
                                      DatabaseReference msg = ref.child(uniqueNo);
                                      msg.addChildEventListener(new ChildEventListener() {
                                          @Override
                                          public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                              size[0]++;
                                              if (arrayListChat.size() < size[0]) {
                                                  FeedItemChat feedItemChat = new FeedItemChat();
                                                  if (clsSharePreference.GetSharPrf(getString(R.string.SharPrfMobileNo)).equals(String.valueOf(dataSnapshot.child("senderMobNo").getValue()))) {
                                                      feedItemChat.setStringFlag("1");
                                                      feedItemChat.setStringImgPath(clsSharePreference.GetSharPrf(getString(R.string.SharPrfProImg)));
                                                  } else {
                                                      feedItemChat.setStringFlag("2");
                                                      feedItemChat.setStringImgPath(stringImgProf);
                                                  }
                                                  feedItemChat.setStringMsg(String.valueOf(dataSnapshot.child("message").getValue()));
                                                  feedItemChat.setStringTime(String.valueOf(dataSnapshot.child("time").getValue()));
                                                  feedItemChat.setStringType(String.valueOf(dataSnapshot.child("type").getValue()));
                                                  arrayListChat.add(feedItemChat);
                                                  chatAdapter = new ChatAdapter(MainActivityChat.this, arrayListChat);
                                                  listViewChat.setAdapter(chatAdapter);
                                                  chatAdapter.notifyDataSetChanged();

                                              }
                                          }

                                          @Override
                                          public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                          }

                                          @Override
                                          public void onChildRemoved(DataSnapshot dataSnapshot) {

                                          }

                                          @Override
                                          public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                          }

                                          @Override
                                          public void onCancelled(DatabaseError databaseError) {

                                          }
                                      });*/
    }

    @SuppressLint("RtlHardcoded")
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

                Date dt = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS aa");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm aa");
                String time = sdf.format(dt);
                time =time.replace(":","_");
                time =time.replace("-","_");
                time = time.replace(".","_");
                time = time.replace(" ","_");
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("ChatMsg");

                DatabaseReference usersRef = ref.child(uniqueNo);

                Map<String, String> users = new HashMap<>();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    listViewChat.scrollListBy(arrayListChat.size()-1);
                }

                users.put("message", editTextMsg.getText().toString());
                users.put("ReceiverMobileNo", String.valueOf(expertMobNo));
                users.put("readValue","0");
                users.put("senderMobNo",clsSharePreference.GetSharPrf(getString(R.string.SharPrfMobileNo)));
                String time2 = sdf2.format(dt);
                users.put("time",time2);
                users.put("key","Time"+time+"_"+clsSharePreference.GetSharPrf(getString(R.string.SharPrfUID)));
                users.put("type","1");
                usersRef.push().setValue(users);
                FeedItemChat feedItemChat = new FeedItemChat();
                feedItemChat.setStringMsg(editTextMsg.getText().toString());
                feedItemChat.setStringMobileNo(clsSharePreference.GetSharPrf(getString(R.string.SharPrfMobileNo)));
                feedItemChat.setStringReadValue("0");
                feedItemChat.setStringTime(time2);
                feedItemChat.setStringKeyValue("Time"+time+"_"+clsSharePreference.GetSharPrf(getString(R.string.SharPrfUID)));
                feedItemChat.setStringType("1");
                feedItemChat.setStringImg(clsSharePreference.GetSharPrf(getString(R.string.SharPrfProImg)));
               // sqlDatabaseChat.CreateTable("TABLE"+String.valueOf(uniqueNo),feedItemChat);
                editTextMsg.setText("");
                arrayListChat = sqlDatabaseChat.GetChatMsg("TABLE"+String.valueOf(uniqueNo));
                chatAdapter = new ChatAdapter(MainActivityChat.this, arrayListChat);
                listViewChat.setAdapter(chatAdapter);

            }
        }
        if(v==imageButtonAttach){
            LayoutInflater layoutInflater = getLayoutInflater();
            View viewPop = layoutInflater.inflate(R.layout.attach_layout,null);
            Rect location = locateView(imageButtonAttach);
            final PopupWindow popupWindow = new PopupWindow(viewPop);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            // Removes default background.
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            if (location != null) {
                popupWindow.showAtLocation(imageButtonAttach,Gravity.CENTER|Gravity.TOP,0,location.bottom);
            }
            if (location != null) {
                popupWindow.update(0,location.bottom,width,(int)(height*0.3));
            }
            ImageButton imageButtonCamera = (ImageButton)viewPop.findViewById(R.id.imageButtonCamera);
            ImageButton imageButtonGallery = (ImageButton)viewPop.findViewById(R.id.imageButtonGallery);
            imageButtonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    String path = Environment.getExternalStorageDirectory()+ "/Images";

                    File imgFile = new File(path);
                    if (!imgFile.exists()) {
                        File wallpaperDirectory = new File("/sdcard/Images/");
                        wallpaperDirectory.mkdirs();
                    }
                    File file = new File(new File("/sdcard/Images/"), "one.jpg");
                    Uri uri = Uri.fromFile(file);
                    try {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(takePicture, 0);
                    }catch (Exception e){
                        Toast.makeText(MainActivityChat.this,"Please Check Permission",Toast.LENGTH_SHORT).show();
                    }

                }
            });
            imageButtonGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                }
            });

        }
    }
    public static Rect locateView(View v)
    {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try
        {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe)
        {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                   // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    String path = Environment.getExternalStorageDirectory()+ "/Images";

                    File imgFile = new File(path);
                    if (!imgFile.exists()) {
                        File wallpaperDirectory = new File("/sdcard/Images/");
                        wallpaperDirectory.mkdirs();
                    }
                    File file = new File(new File("/sdcard/Images/"), "one.jpg");
                    Uri uri = Uri.fromFile(file);

                    //Convert uri to bitmap and reduce its size
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                        verifyStoragePermissions(this);
                        break;
                    }
                    //scale bitmap to the required size of imageview
                    //this way it memory usage will also be less
                    //set resized bitmap to imageview
                    bitmap = Bitmap.createScaledBitmap(bitmap, 750, 700, false);
                    SendImgTo(bitmap);

                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        SendImgTo(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;


        }
    }
    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    //persmission method.
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
    public void SendImgTo(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final Date dt = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS aa");
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm aa");
        String time = sdf.format(dt);
        time =time.replace(":","_");
        time =time.replace("-","_");
        time = time.replace(".","_");
        time = time.replace(" ","_");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainImagesRef = storageRef.child("ChatImages/"+"UniqueId"+uniqueNo+"/"+time+".jpg");
        UploadTask uploadTask = mountainImagesRef.putBytes(imageBytes);
        final String finalTime = time;
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("image upload",exception.getMessage());
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("ChatMsg");
                DatabaseReference usersRef = ref.child(uniqueNo);

                Map<String, String> users = new HashMap<>();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    listViewChat.scrollListBy(arrayListChat.size()-1);
                }
                users.put("message","ChatImages/"+"UniqueId"+uniqueNo+"/"+ finalTime +".jpg");
                users.put("senderMobNo",clsSharePreference.GetSharPrf(getString(R.string.SharPrfMobileNo)));
                users.put("ReceiverMobileNo", String.valueOf(expertMobNo));
                users.put("readValue","0");
                users.put("key", finalTime +"-"+clsSharePreference.GetSharPrf(getString(R.string.SharPrfUID)));
                String time2 = sdf2.format(dt);
                users.put("time",time2);
                users.put("type","2");
                users.put("key","Time"+finalTime+"_"+clsSharePreference.GetSharPrf(getString(R.string.SharPrfUID)));
                usersRef.push().setValue(users);
                usersRef.push().setValue(users);
                FeedItemChat feedItemChat = new FeedItemChat();
                feedItemChat.setStringMsg("ChatImages/"+"UniqueId"+uniqueNo+"/"+ finalTime +".jpg");
                feedItemChat.setStringMobileNo(clsSharePreference.GetSharPrf(getString(R.string.SharPrfMobileNo)));
                feedItemChat.setStringReadValue("0");
                feedItemChat.setStringTime(time2);
                feedItemChat.setStringKeyValue("Time"+finalTime+"_"+clsSharePreference.GetSharPrf(getString(R.string.SharPrfUID)));
                feedItemChat.setStringType("2");
                feedItemChat.setStringImg(clsSharePreference.GetSharPrf(getString(R.string.SharPrfProImg)));
                sqlDatabaseChat.CreateTable("TABLE"+String.valueOf(uniqueNo),feedItemChat);
                arrayListChat = sqlDatabaseChat.GetChatMsg("TABLE"+String.valueOf(uniqueNo));
                chatAdapter = new ChatAdapter(MainActivityChat.this, arrayListChat);
                listViewChat.setAdapter(chatAdapter);
            }
        });

    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void RefreshAdapter() {
        arrayListChat = sqlDatabaseChat.GetChatMsg("TABLE"+String.valueOf(uniqueNo));
        chatAdapter = new ChatAdapter(MainActivityChat.this, arrayListChat);
        listViewChat.setAdapter(chatAdapter);
    }
}
