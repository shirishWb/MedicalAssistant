package com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment.ListBySpecializationAndName.BySpecialization.FeedItemListBySpecialization;
import com.whitebirdtechnology.medicalassistant.R;
import com.whitebirdtechnology.medicalassistant.Server.BackgroundTaskFragment;
import com.whitebirdtechnology.medicalassistant.Server.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by dell on 23/3/17.
 */

public class ByCategoriesTab extends Fragment implements ServerResponse{

    GridView gridViewCategories;
    GridViewCategorieAdapter gridViewCategorieAdapter;
    ArrayList<FeedListItemCat> arrayList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_tab,container,false);
        gridViewCategories = (GridView)view.findViewById(R.id.gridViewCategory);
        arrayList = new ArrayList<>();
        gridViewCategorieAdapter = new GridViewCategorieAdapter(getActivity(),arrayList);
        gridViewCategories.setAdapter(gridViewCategorieAdapter);

        HashMap<String,String> params = new HashMap<>();
        params.put(getString(R.string.serviceKeyFlagCate),getString(R.string.expertType));
        new BackgroundTaskFragment(this,params,getString(R.string.categoryURL)).execute();
        return view;
    }

    @Override
    public void Response(String result, String methodKey) {
        Log.d("resulCate",result);
        try {
            JSONObject object = new JSONObject(result);
            String success = object.getString(getString(R.string.serviceKeySuccess));
            String flagCate = object.getString(getString(R.string.serviceKeyFlagCate));
            if(flagCate.equals("1")){
                if(success.equals(getString(R.string.serviceKeySuccessValue))){
                    JSONArray jsonArray = object.getJSONArray(getString(R.string.serviceKeyCateList));
                    for(int i=0;i<jsonArray.length();i++){
                        FeedListItemCat feedListItemCat = new FeedListItemCat();
                        JSONObject object1 = jsonArray.getJSONObject(i);
                        feedListItemCat.setStringCatName(object1.getString(getString(R.string.serviceKeyCateName)));
                        feedListItemCat.setStringCatId(object1.getString(getString(R.string.serviceKeyCateId)));
                        arrayList.add(feedListItemCat);
                    }
                    gridViewCategorieAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
