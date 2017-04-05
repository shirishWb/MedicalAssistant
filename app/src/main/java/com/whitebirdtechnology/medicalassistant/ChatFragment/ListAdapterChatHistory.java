package com.whitebirdtechnology.medicalassistant.ChatFragment;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitebirdtechnology.medicalassistant.R;

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
        FeedItemChatHistory feedItemChatHistory = (FeedItemChatHistory) getItem(position);
        viewHolder.name.setText(feedItemChatHistory.getStringAnotherUser());
        viewHolder.msg.setText(feedItemChatHistory.getStringLastMsg());
        viewHolder.time.setText(feedItemChatHistory.getStringTime());
        return v;
        }
}
