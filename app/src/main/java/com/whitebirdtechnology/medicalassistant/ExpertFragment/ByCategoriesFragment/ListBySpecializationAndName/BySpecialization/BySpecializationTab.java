package com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment.ListBySpecializationAndName.BySpecialization;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.whitebirdtechnology.medicalassistant.R;
import com.whitebirdtechnology.medicalassistant.Server.BackgroundTaskFragment;
import com.whitebirdtechnology.medicalassistant.Server.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dell on 27/3/17.
 */

public class BySpecializationTab extends Fragment implements ServerResponse{
    ListView listView;
    BySpecializationAdapter bySpecializationAdapter;
    ArrayList<FeedItemListBySpecialization> arrayList;
    String selCat;

    @SuppressLint("ValidFragment")
    public BySpecializationTab(String selCat) {
        this.selCat = selCat;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.by_specialization_tab,container,false);
        listView = (ListView)view.findViewById(R.id.listViewByCategories);
        arrayList = new ArrayList<>();
        bySpecializationAdapter = new BySpecializationAdapter(getActivity(),arrayList);
        listView.setAdapter(bySpecializationAdapter);
        HashMap<String,String> params = new HashMap<>();
        params.put(getString(R.string.serviceKeyFlagCate),"2");
        params.put(getString(R.string.serviceKeyCateId),selCat);
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
            if(flagCate.equals("2")){
                if(success.equals(getString(R.string.serviceKeySuccessValue))){
                    JSONArray jsonArray = object.getJSONArray(getString(R.string.serviceKeySubCateList));
                    for(int i=0;i<jsonArray.length();i++){
                        FeedItemListBySpecialization feedItemListBySpecialization = new FeedItemListBySpecialization();
                        JSONObject object1 = jsonArray.getJSONObject(i);
                        feedItemListBySpecialization.setStringSubCatName(object1.getString(getString(R.string.serviceKeySubCateName)));
                        feedItemListBySpecialization.setStringSubCatId(object1.getString(getString(R.string.serviceKeySubCateId)));
                        arrayList.add(feedItemListBySpecialization);
                    }
                    bySpecializationAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
