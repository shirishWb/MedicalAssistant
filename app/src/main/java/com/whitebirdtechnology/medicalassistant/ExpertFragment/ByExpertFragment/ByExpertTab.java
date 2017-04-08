package com.whitebirdtechnology.medicalassistant.ExpertFragment.ByExpertFragment;


import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.whitebirdtechnology.medicalassistant.R;
import com.whitebirdtechnology.medicalassistant.Server.BackgroundTaskFragment;
import com.whitebirdtechnology.medicalassistant.Server.ServerResponse;
import com.whitebirdtechnology.medicalassistant.Sharepreference.ClsSharePreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by dell on 23/3/17.
 */

public class ByExpertTab extends Fragment implements ServerResponse,AbsListView.OnScrollListener {
    ListView listViewExpert;
    ListAdapterByExpert listAdapterByExpert;
    int item =0;
    ClsSharePreference clsSharePreference;
    boolean loading =false;
    int previousTotal = 0;
    int visibleThrishold =5;
    View footerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.by_expert_tab,container,false);
        setHasOptionsMenu(true);
        listViewExpert = (ListView)view.findViewById(R.id.listViewExpert);
        clsSharePreference = new ClsSharePreference(getActivity());
        SingListByExpert.getInstance().arrayListByExpert.clear();
        item =0;

        loading =false;
        previousTotal = 0;
        visibleThrishold =5;
        HashMap<String,String> params = new HashMap<>();
        params.put(getString(R.string.serviceKeyUID),clsSharePreference.GetSharPrf(getString(R.string.SharPrfUID)));
        params.put(getString(R.string.serviceKeyItem), String.valueOf(item));
        params.put(getString(R.string.serviceKeyCateId),"0");
        new BackgroundTaskFragment(this,params,getString(R.string.byExpertURL)).execute();
        /*arrayList = new ArrayList<>();
        for(int i=0;i<5;i++) {
            FeedListByExpert feedListByExpert = new FeedListByExpert();
            feedListByExpert.setStringExpertName("Dr. sushant Rajput");
            arrayList.add(feedListByExpert);

        }*/
        listAdapterByExpert = new ListAdapterByExpert(getActivity(),SingListByExpert.getInstance().arrayListByExpert);
        listViewExpert.setAdapter(listAdapterByExpert);
        footerView = inflater.inflate(R.layout.progress_bar_list,container,false);
        listViewExpert.addFooterView(footerView,null,true);
        listViewExpert.setOnScrollListener(this);
        return view;
    }

    @Override
    public void Response(String result, String methodKey) {
        Log.d("result list",result);
        try {
            JSONObject object = new JSONObject(result);
            String success = object.getString(getString(R.string.serviceKeySuccess));
            if(success.equals(getString(R.string.serviceKeySuccessValue))) {
                JSONArray jsonArray = object.getJSONArray(getString(R.string.serviceKeyArrayExpert));
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object1 = jsonArray.getJSONObject(i);
                    FeedListByExpert feedListByExpert = new FeedListByExpert();
                    feedListByExpert.setStringExpertName(object1.getString(getString(R.string.serviceKeyName)));
                    String img = object1.getString(getString(R.string.serviceKeyImageProf));
                    img.replace("\\","");
                    feedListByExpert.setStringImgPath(img);
                    feedListByExpert.setStringLoc(object1.getString(getString(R.string.serviceKeyLoc)));
                    feedListByExpert.setStringCategory(object1.getString(getString(R.string.serviceKeyCateName)));
                    feedListByExpert.setStringCatId(object1.getString(getString(R.string.serviceKeyCateId)));
                    feedListByExpert.setStringSubCatagory(object1.getString(getString(R.string.serviceKeySubCateName)));
                    feedListByExpert.setStringSubCatId(object1.getString(getString(R.string.serviceKeySubCateId)));
                     feedListByExpert.setStringExpertId(object1.getString(getString(R.string.serviceKeyExpertId)));
                    feedListByExpert.setStringMobNo(object1.getString(getString(R.string.serviceKeyMobileNo)));
                   if(object1.getString(getString(R.string.serviceKeyFav)).equals("1"))
                        feedListByExpert.setaBooleanFav(true);
                       else
                        feedListByExpert.setaBooleanFav(false);
                    if(!SingListByExpert.getInstance().arrayListByExpert.contains(feedListByExpert))
                    SingListByExpert.getInstance().arrayListByExpert.add(feedListByExpert);
                }
                listAdapterByExpert.notifyDataSetChanged();


            }else
                listViewExpert.removeFooterView(footerView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(loading){
            if(totalItemCount>previousTotal) {
                previousTotal = totalItemCount;
                loading =false;
            }
        }
        if(!loading&&(totalItemCount-visibleItemCount)<=(firstVisibleItem+visibleThrishold)){
            item++;
            HashMap<String,String> params = new HashMap<>();
            params.put(getString(R.string.serviceKeyUID),clsSharePreference.GetSharPrf(getString(R.string.SharPrfUID)));
            params.put(getString(R.string.serviceKeyItem), String.valueOf(item));
            params.put(getString(R.string.serviceKeyCateId),"0");
            new BackgroundTaskFragment(this,params,getString(R.string.byExpertURL)).execute();
            loading =true;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        try {
            // Associate searchable configuration with the SearchView
            SearchManager searchManager =
                    (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =
                    (SearchView) menu.findItem(R.id.menuItemSearch).getActionView();
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    // do your search
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    Log.d("search",s);
                    // do your search on change or save the last string or...
                    return false;
                }
            });


        }catch(Exception e){e.printStackTrace();}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItemSearch:
                break;
        }
        return true;
    }
}
