package com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment.ListBySpecializationAndName.BySpecialization.SubCategoryExpertList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.whitebirdtechnology.medicalassistant.R;
import com.whitebirdtechnology.medicalassistant.Server.BackgroundTask;
import com.whitebirdtechnology.medicalassistant.Server.BackgroundTaskFragment;
import com.whitebirdtechnology.medicalassistant.Server.ServerResponse;
import com.whitebirdtechnology.medicalassistant.Sharepreference.ClsSharePreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivitySortedListBySubCate extends AppCompatActivity implements ServerResponse, AbsListView.OnScrollListener {
    ActionBar.LayoutParams params;
    TextView Title;
    ListView listViewExpert;
    AdapterListSubCat adapterListSubCat;
    int item =0;
    ClsSharePreference clsSharePreference;
    boolean loading =false;
    int previousTotal = 0;
    int visibleThrishold =5;
    String selCat;
    View footerView;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.action_bar_layout, null);
        params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        Title = (TextView) view.findViewById(R.id.actionbar_title);
        Title.setText("Consult Doctor");

        getSupportActionBar().setCustomView(view,params);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_main_sorted_list_by_sub_cate);
        Intent intent = getIntent();
        selCat = intent.getStringExtra("CatId");
        Title.setText("Consult "+intent.getStringExtra("CatName"));
        listViewExpert = (ListView)findViewById(R.id.listViewExpertSub);
        clsSharePreference = new ClsSharePreference(this);
        SingltonByExpertSubCat.getInstance().arrayListByExpertName.clear();
        item =0;
        loading =false;
        previousTotal = 0;
        visibleThrishold =5;
        HashMap<String,String> params = new HashMap<>();
        params.put(getString(R.string.serviceKeyUID),clsSharePreference.GetSharPrf(getString(R.string.SharPrfUID)));
        params.put(getString(R.string.serviceKeyItem), String.valueOf(item));
        params.put(getString(R.string.serviceKeyCateId),"-1");
        params.put(getString(R.string.serviceKeySubCateId),selCat);
        new BackgroundTask(this,params,getString(R.string.byExpertURL)).execute();
        adapterListSubCat = new AdapterListSubCat(this,SingltonByExpertSubCat.getInstance().arrayListByExpertName);
        listViewExpert.setAdapter(adapterListSubCat);
        LayoutInflater inflater = this.getLayoutInflater();
        footerView = inflater.inflate(R.layout.progress_bar_list,null);
        listViewExpert.addFooterView(footerView,null,true);
        listViewExpert.setOnScrollListener(this);
    }
    @Override
    public void Response(String result, String methodKey) {
        try {
            JSONObject object = new JSONObject(result);
            String success = object.getString(getString(R.string.serviceKeySuccess));
            if(success.equals(getString(R.string.serviceKeySuccessValue))) {
                JSONArray jsonArray = object.getJSONArray(getString(R.string.serviceKeyArrayExpert));
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object1 = jsonArray.getJSONObject(i);
                    FeedListBySubCatExpert feedListByExpert = new FeedListBySubCatExpert();
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
                    if(object1.getString(getString(R.string.serviceKeyFav)).equals("1"))
                        feedListByExpert.setaBooleanFav(true);
                    else
                        feedListByExpert.setaBooleanFav(false);
                    if(!SingltonByExpertSubCat.getInstance().arrayListByExpertName.contains(feedListByExpert))
                        SingltonByExpertSubCat.getInstance().arrayListByExpertName.add(feedListByExpert);
                }
                adapterListSubCat.notifyDataSetChanged();


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
            params.put(getString(R.string.serviceKeyCateId),"-1");
            params.put(getString(R.string.serviceKeySubCateId),selCat);
            new BackgroundTask(this,params,getString(R.string.byExpertURL)).execute();
            loading =true;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
