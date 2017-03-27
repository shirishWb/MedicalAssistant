package com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.whitebirdtechnology.medicalassistant.R;


/**
 * Created by dell on 23/3/17.
 */

public class ByCategoriesTab extends Fragment {
    ListView listView;
    BySpecilizationAdapter bySpecilizationAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.by_categories_tab,container,false);
        listView = (ListView)view.findViewById(R.id.listViewByCategories);
        bySpecilizationAdapter = new BySpecilizationAdapter(getActivity());
        listView.setAdapter(bySpecilizationAdapter);

        return view;
    }
}
