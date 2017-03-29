package com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment.ListBySpecializationAndName.ByExpertName;

import android.app.Activity;
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

import com.whitebirdtechnology.medicalassistant.ChatScreen.MainActivityChat;
import com.whitebirdtechnology.medicalassistant.R;

import java.util.ArrayList;

/**
 * Created by dell on 27/3/17.
 */

public class ListAdapterByExpertName extends ArrayAdapter{
    Activity activity;
    public ListAdapterByExpertName(@NonNull Activity activity, @NonNull ArrayList<FeedItemListByExpertName> objects) {
        super(activity, 0, objects);
        this.activity =activity;
    }
    private static class ViewHolder {
        TextView name,occupation,loc;
        ImageView imageViewProf;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.by_expert_list_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.textViewName);
            viewHolder.occupation = (TextView)convertView.findViewById(R.id.textViewOccupation);
            viewHolder.loc = (TextView)convertView.findViewById(R.id.textViewLoc);
            viewHolder.imageViewProf = (ImageView) convertView.findViewById(R.id.imageViewProf);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final FeedItemListByExpertName feedListByExpert = (FeedItemListByExpertName) getItem(position);
        viewHolder.name.setText(feedListByExpert.getStringExpertName());
        viewHolder.loc.setText(feedListByExpert.getStringLoc());
        viewHolder.occupation.setText(feedListByExpert.getStringCategory()+","+feedListByExpert.getStringSubCatagory());
        if (feedListByExpert.getStringImgPath() != null) {
            final String stringImageURL = feedListByExpert.getStringImgPath();
            if(!stringImageURL.isEmpty()) {
               /* Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // new FirstTask(stringImageURL,holder.imageViewAll).execute();
                        new ImageDownloaderTask(viewHolder.imageViewProf, stringImageURL, activity).execute();

                    }
                });
                thread.start();*/
                byte[] decodedString = Base64.decode(feedListByExpert.getStringImgPath(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                viewHolder.imageViewProf.setImageBitmap(decodedByte);

            }
        } else
            viewHolder.imageViewProf.setVisibility(View.GONE);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivityChat.class);
                Bundle bundle = new Bundle();
                bundle.putString("EName",feedListByExpert.getStringExpertName());
                bundle.putString("EOccupation",feedListByExpert.getStringCategory()+","+feedListByExpert.getStringSubCatagory());
                bundle.putString("EImg",feedListByExpert.getStringImgPath());
                bundle.putString("EId",feedListByExpert.getStringExpertId());
                bundle.putBoolean("BoolFav",feedListByExpert.getaBooleanFav());
                intent.putExtra("BundleExpert",bundle);
                activity.startActivity(intent);
            }
        });
        return convertView;
    }
}
