package com.whitebirdtechnology.medicalassistant.HomeFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.whitebirdtechnology.medicalassistant.R;

/**
 * Created by dell on 22/3/17.
 */

public class HomeTab extends Fragment {
    GridView gridViewCategories;
    GridViewCategorieAdapter gridViewCategorieAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_tab,container,false);
        gridViewCategories = (GridView)view.findViewById(R.id.gridViewCategory);
        gridViewCategorieAdapter = new GridViewCategorieAdapter(getActivity());
        gridViewCategories.setAdapter(gridViewCategorieAdapter);
        return view;
    }
}
