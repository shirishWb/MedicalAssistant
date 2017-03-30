package com.whitebirdtechnology.medicalassistant.ChatScreen.BookAppointmentPage.AppointmentTime;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
 * Created by dell on 30/3/17.
 */

public class GridTimeAdapter extends ArrayAdapter {
    Activity activity;
    public GridTimeAdapter(@NonNull Activity activity, @NonNull ArrayList<String> objects) {
        super(activity, 0, objects);
        this.activity = activity;
    }
    private static class ViewHolder {
        TextView time;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.grid_view_time_item, parent, false);
            viewHolder.time = (TextView) convertView.findViewById(R.id.textViewTime);
            viewHolder.time.setText((CharSequence) getItem(position));
            viewHolder.time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                    alertDialog.setMessage("Your Appointment is set at"+(CharSequence) getItem(position));
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    });
                    alertDialog.setNegativeButton("No",null);
                    alertDialog.show();
                }
            });
        }else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

            return convertView;
    }
}
