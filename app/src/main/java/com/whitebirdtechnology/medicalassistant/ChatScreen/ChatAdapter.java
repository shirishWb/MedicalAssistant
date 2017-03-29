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
import android.widget.ImageView;
import android.widget.TextView;

import com.whitebirdtechnology.medicalassistant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 29/3/17.
 */

public class ChatAdapter extends ArrayAdapter {
    public ChatAdapter(@NonNull Activity activity, @NonNull ArrayList<FeedItemChat> objects) {
        super(activity, 0, objects);
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
                LayoutInflater inflater = LayoutInflater.from(getContext());
                if(feedItemChat.getStringFlag().equals("1")) {
                    convertView = inflater.inflate(R.layout.chat_item_send, parent, false);
                    viewHolder.imageViewProf = (ImageView)convertView.findViewById(R.id.imageViewProfChatSend);

                    viewHolder.msg = (TextView)convertView.findViewById(R.id.textViewChatMsg);
                    viewHolder.time = (TextView)convertView.findViewById(R.id.textViewChatTime);
                }else {
                    convertView = inflater.inflate(R.layout.chat_item_received, parent, false);
                    viewHolder.imageViewProf = (ImageView)convertView.findViewById(R.id.imageViewProfChatReceive);

                    viewHolder.msg = (TextView)convertView.findViewById(R.id.textViewChatMsgReceive);
                    viewHolder.time = (TextView)convertView.findViewById(R.id.textViewChatTimeReceive);
                }
                if (feedItemChat.getStringImgPath() != null) {
                    final String stringImageURL = feedItemChat.getStringImgPath();
                    if(!stringImageURL.isEmpty()) {
                        byte[] decodedString = Base64.decode(feedItemChat.getStringImgPath(), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        viewHolder.imageViewProf.setImageBitmap(decodedByte);

                    }
                } else
                    viewHolder.imageViewProf.setVisibility(View.GONE);
                viewHolder.msg.setText(feedItemChat.getStringMsg());
                viewHolder.time.setText(feedItemChat.getStringTime());


            }else {
               viewHolder = (ViewHolder) convertView.getTag();
            }


        return convertView;
    }
}