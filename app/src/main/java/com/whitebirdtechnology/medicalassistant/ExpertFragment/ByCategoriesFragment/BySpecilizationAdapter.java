package com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitebirdtechnology.medicalassistant.R;

/**
 * Created by dell on 25/3/17.
 */

public class BySpecilizationAdapter extends BaseAdapter {
    String [] stringNameCate = new String[]{"Cardiologist", "Dental", "Dermatologist", "Psychiatrist", "Gynaecologist", "Endocrinologist"};
    int[] ints = new int[]{R.drawable.plus_icon,R.drawable.fav_icon_blue,R.drawable.chat_icon_blue,R.drawable.close_icon,R.drawable.category_icon,R.drawable.expert_icon_blue};
    Activity activity;
    public BySpecilizationAdapter(Activity activity){
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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(activity);
        convertView = inflater.inflate(R.layout.by_specialization_item, parent, false);
        TextView textViewName = (TextView)convertView.findViewById(R.id.textViewName);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageViewProf);
        textViewName.setText(stringNameCate[position]);
        imageView.setImageDrawable(activity.getResources().getDrawable(ints[position]));

        return convertView;
    }
}
