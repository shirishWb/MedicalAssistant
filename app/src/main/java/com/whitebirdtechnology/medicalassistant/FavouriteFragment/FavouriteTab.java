package com.whitebirdtechnology.medicalassistant.FavouriteFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whitebirdtechnology.medicalassistant.R;

/**
 * Created by dell on 22/3/17.
 */

public class FavouriteTab extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite_tab,container,false);

        return view;
    }
}
