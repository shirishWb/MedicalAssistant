package com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment.ListBySpecializationAndName.MainActivitySortedListByCate;
import com.whitebirdtechnology.medicalassistant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 25/3/17.
 */

public class GridViewCategorieAdapter extends ArrayAdapter {
    Activity activity;

    public GridViewCategorieAdapter(@NonNull Activity activity, @NonNull ArrayList<FeedListItemCat> objects) {
        super(activity, 0, objects);
        this.activity = activity;
    }
    /*
    public GridViewCategorieAdapter(Activity activity){
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return stringNameCate.length;
    }

    @Override
    public Object getItem(int position) {
        return stringNameCate[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
*/
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(activity);
            convertView = inflater.inflate(R.layout.categories_item, parent, false);
        TextView textViewName = (TextView)convertView.findViewById(R.id.textViewCategories);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageVewIconCate);
        final FeedListItemCat feedListItemCat = (FeedListItemCat) getItem(position);

        textViewName.setText(feedListItemCat.getStringCatName());
       // imageView.setImageDrawable(activity.getResources().getDrawable(ints[position]));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivitySortedListByCate.class);
                intent.putExtra("CatId",feedListItemCat.getStringCatId());
                intent.putExtra("CatName",feedListItemCat.getStringCatName());
                activity.startActivity(intent);
            }
        });
        return convertView;
    }
}
