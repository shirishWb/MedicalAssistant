package com.whitebirdtechnology.medicalassistant.SignUpScreen;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.whitebirdtechnology.medicalassistant.R;
import com.whitebirdtechnology.medicalassistant.Server.BackgroundTask;
import com.whitebirdtechnology.medicalassistant.Server.ServerResponse;
import com.whitebirdtechnology.medicalassistant.Sharepreference.ClsSharePreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivitySignUp extends AppCompatActivity implements View.OnClickListener, ServerResponse, AdapterView.OnItemSelectedListener {
    EditText editTextName,editTextEmail,editTextPassword,editTextMobNo;
    TextView textViewSignup;
    String stringEmail,stringPassword,stringMobileNo,stringName,stringURI,stringImg,stringCate,stringSubCate;
    ImageView imageViewProfile;
    Spinner spinnerCategorie,spinnerSubCategories;
    HashMap<String,String> hashMapCate,hashMapSubCate;
    ArrayAdapter<String> adapterCate,adapterSubCate;
    ArrayList<String> arrayListCate,arrayListSubCate;
    Dialog alertDialogDismiss;
    ClsSharePreference clsSharePreference;
    String userType;
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
        Title.setText("Sign Up");

        getSupportActionBar().setCustomView(view,params);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_main_sign_up);
        clsSharePreference = new ClsSharePreference(this);
        imageViewProfile = (ImageView)findViewById(R.id.imageViewProf);
        editTextEmail= (EditText)findViewById(R.id.editTextEmail);
        editTextMobNo= (EditText)findViewById(R.id.editTextMobNo);
        editTextName= (EditText)findViewById(R.id.editTextName);
        editTextPassword= (EditText)findViewById(R.id.editTextPassword);
        textViewSignup =(TextView)findViewById(R.id.textViewSignUp);
        spinnerCategorie = (Spinner)findViewById(R.id.spinnerCategoriesExpert);
        spinnerSubCategories = (Spinner)findViewById(R.id.spinnerSubCategoriesExpert);
        hashMapCate = new HashMap<>();
        hashMapSubCate = new HashMap<>();
        arrayListCate = new ArrayList<>();
        arrayListCate.add("- select Category -");
        arrayListSubCate = new ArrayList<>();
        arrayListSubCate.add("- select SubCategory -");
        adapterCate = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,arrayListCate);
        adapterSubCate = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,arrayListSubCate);
        spinnerCategorie.setAdapter(adapterCate);
        spinnerSubCategories.setAdapter(adapterSubCate);
        spinnerSubCategories.setOnItemSelectedListener(this);
        spinnerCategorie.setOnItemSelectedListener(this);
        HashMap<String,String> params = new HashMap<>();
        params.put(getString(R.string.serviceKeyFlagCate),getString(R.string.expertType));

        textViewSignup.setOnClickListener(this);
        imageViewProfile.setOnClickListener(this);
        userType = clsSharePreference.GetSharPrf(getString(R.string.SharPrfUserType));
        if(!userType.equals("1")){
            spinnerCategorie.setVisibility(View.GONE);
            spinnerSubCategories.setVisibility(View.GONE);
        }else
            new BackgroundTask(this,params,getString(R.string.categoryURL)).execute();

    }
    boolean isEmailValid() {
        stringEmail = editTextEmail.getText().toString();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(stringEmail).matches();
    }
    boolean isPasswordMatches(){
        boolean match =false;
        stringPassword =editTextPassword.getText().toString();


            if (stringPassword.length()>=4){
                match =true;
            }else {
                Toast.makeText(this,"Enter Password at least 4 character",Toast.LENGTH_SHORT).show();
            }

        if(stringPassword.isEmpty()){
            Toast.makeText(this,"Enter Password ",Toast.LENGTH_SHORT).show();
            match =false;
        }

        return match;
    }
    boolean checkMobileNo(){
        boolean MobileMatch =false;
        stringMobileNo =editTextMobNo.getText().toString();

            if (stringMobileNo.length()==10) {

                if(android.util.Patterns.PHONE.matcher(stringMobileNo).matches()){
                    MobileMatch = true;
                }else
                    Toast.makeText(this,"Mobile Number Is Not Valid",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Enter 10 digit Mobile Number", Toast.LENGTH_SHORT).show();
            }


        return MobileMatch;
    }

    public boolean isCheckName() {
        stringName =editTextName.getText().toString();
        return stringName.matches("[a-z A-Z]+");
    }
    public boolean isCategory(){
        Boolean result = false;
        stringCate =String.valueOf(spinnerCategorie.getSelectedItem());
        stringSubCate = String.valueOf(spinnerSubCategories.getSelectedItem());
        if(hashMapSubCate.containsKey(stringSubCate)){
            result =true;
        }
        return result;
    }
    @Override
    public void onClick(View v) {
        if (v == imageViewProfile) {
            selectImage();
        }

        if (v == textViewSignup) {
            if(TextUtils.isEmpty(stringURI)){
                Toast.makeText(this, "Please Select Profile picture", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isCheckName()) {
                if (stringName.isEmpty()) {
                    Toast.makeText(this, "Enter Name ", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(this, "Please Enter Your Correct Name", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (!isEmailValid()) {
                Toast.makeText(this, "Enter Valid Email Id", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isPasswordMatches()) {
                return;
            }

            if (!checkMobileNo()) {
                return;
            }
            HashMap<String, String> parameters = new HashMap<String, String>();
            if(userType.equals(getString(R.string.expertType))) {
                if (!isCategory()) {
                    Toast.makeText(this, "Select categories to register as expert", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    parameters.put(getString(R.string.serviceKeyCateId),hashMapCate.get(stringCate));
                    parameters.put(getString(R.string.serviceKeySubCateId),hashMapSubCate.get(stringSubCate));
                }
            }
            try
            {
                // Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver() , uri);
                imageViewProfile.buildDrawingCache();
                Bitmap bitmap = imageViewProfile.getDrawingCache();
                stringImg = getStringImage(bitmap);
              //  Log.d("Image String",stringImg);
            }
            catch (Exception e)
            {
                return;   //handle exception
            }

            parameters.put(getString(R.string.serviceKeyEmail), stringEmail);
            parameters.put(getString(R.string.serviceKeyPassword), stringPassword);
            parameters.put(getString(R.string.serviceKeyName), stringName);
            parameters.put(getString(R.string.serviceKeyMobileNo), stringMobileNo);
            parameters.put(getString(R.string.serviceKeyImageProf),stringImg);
            parameters.put(getString(R.string.serviceKeyFlagCate),clsSharePreference.GetSharPrf(getString(R.string.SharPrfUserType)));
            Intent intent = new Intent(MainActivitySignUp.this,MainActivityOTP.class);
            Bundle bundle = new Bundle();
            bundle.putString("IMAGEURI",stringURI);
            bundle.putSerializable("HashMapParams",parameters);
            intent.putExtra("bundle",bundle);
            startActivityForResult(intent,3);
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivitySignUp.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.choose_profile_pick_dialog,null);
      //  view.setBackgroundColor(Color.WHITE);
        builder.setView(view);
        alertDialogDismiss = builder.create();
        //alertDialogDismiss.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView textViewTitle = (TextView)view.findViewById(R.id.textViewTitleDialog);
        textViewTitle.setText("Select Profile Image");
        TextView textViewFromGallery = (TextView) view.findViewById(R.id.textViewFromGallery);
        TextView textViewCamera = (TextView) view.findViewById(R.id.textViewCamera);
        textViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDismiss.dismiss();

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

                  //  takePicture.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(takePicture, 0);
                }catch (Exception e){
                    Toast.makeText(MainActivitySignUp.this,"Please Check Permission",Toast.LENGTH_SHORT).show();
                }

            }
        });
        textViewFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDismiss.dismiss();
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });
        alertDialogDismiss.show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    //   Uri uri =data.getData();
                    //  Toast.makeText(MainHomeScreenActivity.this,uri.toString(),Toast.LENGTH_LONG).show();
                    // Bitmap photo = (Bitmap) data.getExtras().get("data");
                    // StartImageCropActivity(null,photo,"0");
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imageViewProfile.setImageBitmap(photo);
                    /*
                    String path = Environment.getExternalStorageDirectory()+ "/Images";

                    File imgFile = new File(path);
                    if (!imgFile.exists()) {
                        File wallpaperDirectory = new File("/sdcard/Images/");
                        wallpaperDirectory.mkdirs();
                    }
                    File file = new File(new File("/sdcard/Images/"), "one.jpg");
                    Uri uri = Uri.fromFile(file);
                    imageViewProfile.setImageURI(uri);
                    stringURI = uri.toString();*/
                    stringURI = "123";
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    imageViewProfile.setImageURI(selectedImage);
                    stringURI = selectedImage.toString();
                }
                break;
            case 3:
                if(resultCode == RESULT_OK){
                    this.finish();
                }


        }
    }



    @Override
    public void Response(String result, String methodKey) {
        Log.d("resulCate",result);
        try {
            JSONObject object = new JSONObject(result);
            String success = object.getString(getString(R.string.serviceKeySuccess));
            String flagCate = object.getString(getString(R.string.serviceKeyFlagCate));
            if(flagCate.equals("1")){
                if(success.equals(getString(R.string.serviceKeySuccessValue))){
                    JSONArray jsonArray = object.getJSONArray(getString(R.string.serviceKeyCateList));
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject object1 = jsonArray.getJSONObject(i);
                        arrayListCate.add(object1.getString(getString(R.string.serviceKeyCateName)));
                        hashMapCate.put(object1.getString(getString(R.string.serviceKeyCateName)),object1.getString(getString(R.string.serviceKeyCateId)));
                    }
                    adapterCate.notifyDataSetChanged();
                }
            }else if(flagCate.equals("2")){
                if(success.equals(getString(R.string.serviceKeySuccessValue))){
                    JSONArray jsonArray = object.getJSONArray(getString(R.string.serviceKeySubCateList));
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject object1 = jsonArray.getJSONObject(i);
                        arrayListSubCate.add(object1.getString(getString(R.string.serviceKeySubCateName)));
                        hashMapSubCate.put(object1.getString(getString(R.string.serviceKeySubCateName)),object1.getString(getString(R.string.serviceKeySubCateId)));
                    }
                    adapterSubCate.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner)parent;
        if(spinner.getId()==R.id.spinnerCategoriesExpert){
            String sel = String.valueOf(spinner.getSelectedItem());
            if(!hashMapCate.containsKey(sel))
                return;
            HashMap<String,String> params = new HashMap<>();
            params.put(getString(R.string.serviceKeyFlagCate),"2");
            params.put(getString(R.string.serviceKeyCateId),hashMapCate.get(sel));
            new BackgroundTask(this,params,getString(R.string.categoryURL)).execute();
        }else if(spinner.getId()==R.id.spinnerSubCategoriesExpert){

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
