package com.whitebirdtechnology.medicalassistant.ChatScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.whitebirdtechnology.medicalassistant.ChatScreen.FullScreenImageChat.MainActivityFullScreenImgChat;
import com.whitebirdtechnology.medicalassistant.R;
import com.whitebirdtechnology.medicalassistant.Server.ImageDownloaderTask;

import java.util.ArrayList;

/**
 * Created by dell on 29/3/17.
 */

public class ChatAdapter extends ArrayAdapter {
    Activity activity;
    public ChatAdapter(@NonNull Activity activity, @NonNull ArrayList<FeedItemChat> objects) {
        super(activity,0,objects);
        this.activity =activity;
    }
    private static class ViewHolder {
        TextView msg,time;
        ImageView imageViewProf;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

            final ViewHolder viewHolder;
            if(v ==null){
                viewHolder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.chat_item_send, parent,false);
                viewHolder.imageViewProf = (ImageView)v.findViewById(R.id.imageViewProfChatSend);
                viewHolder.msg = (TextView)v.findViewById(R.id.textViewChatMsg);
                viewHolder.time = (TextView)v.findViewById(R.id.textViewChatTime);
                int viewType =getItemViewType(position);
                switch (viewType){
                    case 0:
                        v = inflater.inflate(R.layout.chat_item_send, parent,false);
                        viewHolder.imageViewProf = (ImageView)v.findViewById(R.id.imageViewProfChatSend);
                        viewHolder.msg = (TextView)v.findViewById(R.id.textViewChatMsg);
                        viewHolder.time = (TextView)v.findViewById(R.id.textViewChatTime);
                        break;
                    case 1:
                        v = inflater.inflate(R.layout.chat_item_received, parent,false);
                        viewHolder.imageViewProf = (ImageView)v.findViewById(R.id.imageViewProfChatReceive);
                        viewHolder.msg = (TextView)v.findViewById(R.id.textViewChatMsgReceive);
                        viewHolder.time = (TextView)v.findViewById(R.id.textViewChatTimeReceive);
                        break;
                    case 2:
                        v = inflater.inflate(R.layout.chat_item_img_send,parent,false);
                        viewHolder.imageViewProf = (ImageView)v.findViewById(R.id.imageViewSendImgChat);
                        viewHolder.time = (TextView)v.findViewById(R.id.textViewTime);
                        break;
                    case 3:
                        v = inflater.inflate(R.layout.chat_item_img_received,parent,false);
                        viewHolder.imageViewProf = (ImageView)v.findViewById(R.id.imageViewReceiveImgChat);
                        viewHolder.time = (TextView)v.findViewById(R.id.textViewTime);
                        break;

                }

                v.setTag(viewHolder);

            }else {
               viewHolder = (ViewHolder) v.getTag();
            }
        FeedItemChat feedItemChat = (FeedItemChat) getItem(position);
        switch (feedItemChat.getStringType()){
            case "1":
                final String stringImageURL = feedItemChat.getStringImgPath();
                if(!stringImageURL.isEmpty()) {
                    byte[] decodedString = Base64.decode(feedItemChat.getStringImgPath(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    viewHolder.imageViewProf.setImageBitmap(decodedByte);

                }
                viewHolder.msg.setText(feedItemChat.getStringMsg());
                viewHolder.time.setText(feedItemChat.getStringTime());
                break;
            case "2":

               /* final String stringImage = feedItemChat.getStringMsg();
                if(!stringImage.isEmpty()) {
                    byte[] decodedString = Base64.decode(feedItemChat.getStringMsg(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    viewHolder.imageViewProf.setImageBitmap(decodedByte);

                }*/
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference islandRef = storageRef.child(feedItemChat.getStringMsg());

                final long ONE_MEGABYTE = 800 * 800;
                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(final byte[] bytes) {
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        viewHolder.imageViewProf.setImageBitmap(bitmap);

                        viewHolder.imageViewProf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(activity, MainActivityFullScreenImgChat.class);
                                intent.putExtra("ImgByteArray",bytes);
                                activity.startActivity(intent);
                            }
                        });
                        // Data for "images/island.jpg" is returns, use this as needed
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
                viewHolder.time.setText(feedItemChat.getStringTime());
                break;

        }


        return v;
    }

    @Override
    public int getItemViewType(int position) {
        FeedItemChat feedItemChat = (FeedItemChat) getItem(position);
        if(feedItemChat.getStringFlag().equals("1"))
            switch (feedItemChat.getStringType()){
                case "1":return 0;//Msg Send
                case "2": return 2;//Img Send
            }else {
            switch (feedItemChat.getStringType()){
                case "1": return 1;//Msg Received
                case "2": return 3;//Img Received
            }
        }
        return 4;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }
}
