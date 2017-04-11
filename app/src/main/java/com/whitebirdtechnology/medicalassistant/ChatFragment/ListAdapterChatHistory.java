package com.whitebirdtechnology.medicalassistant.ChatFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.whitebirdtechnology.medicalassistant.ChatScreen.MainActivityChat;
import com.whitebirdtechnology.medicalassistant.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by dell on 5/4/17.
 */

public class ListAdapterChatHistory extends ArrayAdapter {
    Activity activity;
    public ListAdapterChatHistory(@NonNull Activity activity, @NonNull ArrayList<FeedItemChatHistory> objects) {
        super(activity, 0, objects);
        this.activity = activity;
    }
    private static class ViewHolder {
        TextView msg,time,name;
        int position;
        ImageView imageViewProf;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        final ViewHolder viewHolder;
        if(v ==null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.chat_history_item, parent, false);
            viewHolder.imageViewProf = (ImageView) v.findViewById(R.id.imageViewProfChatHistory);
            viewHolder.msg = (TextView) v.findViewById(R.id.textViewLastMsg);
            viewHolder.time = (TextView) v.findViewById(R.id.textViewTime);
            viewHolder.name = (TextView)v.findViewById(R.id.textViewName);
            v.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)v.getTag();
        }
        viewHolder.position = position;
        final FeedItemChatHistory feedItemChatHistory = (FeedItemChatHistory) getItem(viewHolder.position);
        viewHolder.name.setText(feedItemChatHistory.getStringSenderName());
        viewHolder.msg.setText(feedItemChatHistory.getStringLastMsg());
        viewHolder.time.setText(feedItemChatHistory.getStringTime());
       /* FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference islandRef = storageRef.child(feedItemChatHistory.getStringSenderImgPath());

        final long ONE_MEGABYTE = 800 * 800;
        final View finalV = v;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(final byte[] bytes) {
                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);*/
                byte[] decodedString = Base64.decode(feedItemChatHistory.getStringSenderImgPath(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                viewHolder.imageViewProf.setImageBitmap(decodedByte);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, MainActivityChat.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("EName",feedItemChatHistory.getStringSenderName());
                        bundle.putString("EOccupation",feedItemChatHistory.getStringSenderOccu());
                        bundle.putString("EImg",feedItemChatHistory.getStringSenderImgPath());
                        bundle.putString("EId",feedItemChatHistory.getStringSenderId());
                        bundle.putBoolean("BoolFav",feedItemChatHistory.getaBooleanIsFav());
                        bundle.putString("EMobNo",feedItemChatHistory.getStringMobNo());
                        intent.putExtra("BundleExpert",bundle);
                        activity.startActivity(intent);
                    }
                });/*
                // Data for "images/island.jpg" is returns, use this as needed
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
*/
        return v;

        }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
