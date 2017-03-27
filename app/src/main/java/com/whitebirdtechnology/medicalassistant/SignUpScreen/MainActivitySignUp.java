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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.whitebirdtechnology.medicalassistant.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

public class MainActivitySignUp extends AppCompatActivity implements View.OnClickListener{
    EditText editTextName,editTextEmail,editTextPassword,editTextConfirmPass,editTextMobNo;
    TextView textViewSignup;
    String stringEmail,stringPassword,stringConfirmPassword,stringMobileNo,stringName,stringURI,stringImg;
    ImageView imageViewProfile;
    Dialog alertDialogDismiss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sign_up);
        imageViewProfile = (ImageView)findViewById(R.id.imageViewProf);
        editTextConfirmPass = (EditText)findViewById(R.id.editTextConfirmPassword);
        editTextEmail= (EditText)findViewById(R.id.editTextEmail);
        editTextMobNo= (EditText)findViewById(R.id.editTextMobNo);
        editTextName= (EditText)findViewById(R.id.editTextName);
        editTextPassword= (EditText)findViewById(R.id.editTextPassword);
        textViewSignup =(TextView)findViewById(R.id.textViewSignUp);
        textViewSignup.setOnClickListener(this);
        imageViewProfile.setOnClickListener(this);
    }
    boolean isEmailValid() {
        stringEmail = editTextEmail.getText().toString();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(stringEmail).matches();
    }
    boolean isPasswordMatches(){
        boolean match =false;
        stringPassword =editTextPassword.getText().toString();
        stringConfirmPassword =editTextConfirmPass.getText().toString();

        if(stringPassword.equals(stringConfirmPassword)){
            if (stringPassword.length()>=4){
                match =true;
            }else {
                Toast.makeText(this,"Enter Password at least 4 character",Toast.LENGTH_SHORT).show();
            }
        }else
            Toast.makeText(this,"Password not Matched",Toast.LENGTH_SHORT).show();
        if(stringPassword.isEmpty()){
            Toast.makeText(this,"Enter Password ",Toast.LENGTH_SHORT).show();
            match =false;
        }

        return match;
    }
    boolean checkMobileNo(){
        boolean MobileMatch =false;
        stringMobileNo =editTextMobNo.getText().toString();
        if(stringMobileNo.isEmpty()){
            MobileMatch=true;
        }else {
            if (stringMobileNo.length()==10) {

                if(android.util.Patterns.PHONE.matcher(stringMobileNo).matches()){
                    MobileMatch = true;
                }else
                    Toast.makeText(this,"Mobile Number Is Not Valid",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Enter 10 digit Mobile Number", Toast.LENGTH_SHORT).show();
            }
        }

        return MobileMatch;
    }

    public boolean isCheckName() {
        stringName =editTextName.getText().toString();
        return stringName.matches("[a-z A-Z]+");
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
            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put(getString(R.string.serviceKeyEmail), stringEmail);
            parameters.put(getString(R.string.serviceKeyPassword), stringPassword);
            parameters.put(getString(R.string.serviceKeyName), stringName);
            parameters.put(getString(R.string.serviceKeyMoblNo), stringMobileNo);
            parameters.put(getString(R.string.serviceKeyImageProf),stringImg);
            Intent intent = new Intent(MainActivitySignUp.this,MainActivityOTP.class);
            Bundle bundle = new Bundle();
            bundle.putString("IMAGEURI",stringURI);
            bundle.putSerializable("HashMapParams",parameters);
            intent.putExtra("bundle",bundle);
            startActivity(intent);
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


        }
    }

}
