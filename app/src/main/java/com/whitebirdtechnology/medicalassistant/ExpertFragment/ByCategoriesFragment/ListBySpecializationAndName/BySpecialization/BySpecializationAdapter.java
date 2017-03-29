package com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment.ListBySpecializationAndName.BySpecialization;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitebirdtechnology.medicalassistant.R;

import java.util.ArrayList;

/**
 * Created by dell on 25/3/17.
 */

public class BySpecializationAdapter extends ArrayAdapter {
   Activity activity;

    public BySpecializationAdapter(@NonNull Activity activity, @NonNull ArrayList<FeedItemListBySpecialization> objects) {
        super(activity, 0, objects);
        this.activity = activity;
    }
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(activity);
        convertView = inflater.inflate(R.layout.by_specialization_item, parent, false);
        TextView textViewName = (TextView)convertView.findViewById(R.id.textViewName);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageViewProf);
        FeedItemListBySpecialization feedItemListBySpecialization = (FeedItemListBySpecialization) getItem(position);
        textViewName.setText(feedItemListBySpecialization.getStringSubCatName());

        return convertView;
    }
}
