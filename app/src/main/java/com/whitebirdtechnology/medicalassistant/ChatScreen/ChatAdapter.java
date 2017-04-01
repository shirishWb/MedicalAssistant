package com.whitebirdtechnology.medicalassistant.ChatScreen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitebirdtechnology.medicalassistant.R;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

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


        FeedItemChat feedItemChat = (FeedItemChat) getItem(position);
            final ViewHolder viewHolder;
            if(convertView ==null){
                viewHolder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                if(feedItemChat.getStringFlag().equals("1")) {
                    convertView = inflater.inflate(R.layout.chat_item_send, parent,false);
                    viewHolder.imageViewProf = (ImageView)convertView.findViewById(R.id.imageViewProfChatSend);

                    viewHolder.msg = (TextView)convertView.findViewById(R.id.textViewChatMsg);
                    viewHolder.time = (TextView)convertView.findViewById(R.id.textViewChatTime);
                }else {
                    convertView = inflater.inflate(R.layout.chat_item_received, parent,false);
                    viewHolder.imageViewProf = (ImageView)convertView.findViewById(R.id.imageViewProfChatReceive);

                    viewHolder.msg = (TextView)convertView.findViewById(R.id.textViewChatMsgReceive);
                    viewHolder.time = (TextView)convertView.findViewById(R.id.textViewChatTimeReceive);
                }
                convertView.setTag(viewHolder);

            }else {
               viewHolder = (ViewHolder) convertView.getTag();
            }


            final String stringImageURL = feedItemChat.getStringImgPath();
            if(!stringImageURL.isEmpty()) {
                byte[] decodedString = Base64.decode(feedItemChat.getStringImgPath(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                viewHolder.imageViewProf.setImageBitmap(decodedByte);

            }

        viewHolder.msg.setText(feedItemChat.getStringMsg());
        viewHolder.time.setText(feedItemChat.getStringTime());



        return convertView;
    }
}
