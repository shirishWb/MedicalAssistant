package com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment.ListBySpecializationAndName;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.whitebirdtechnology.medicalassistant.ExpertFragment.ExpertPagerAdapter;
import com.whitebirdtechnology.medicalassistant.R;

public class MainActivitySortedListByCate extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    TabLayout.Tab tabByExpert,tabByCategories;
    SortPagerAdapter sortPagerAdapter;
    String selCat;
    ActionBar.LayoutParams params;
    TextView Title;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.light_blue_chat));
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.light_blue_chat));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_blue_chat)));
        getSupportActionBar().setCustomView(view,params);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_main_sorted_list_by_cate);
        tabLayout = (TabLayout)findViewById(R.id.tabLayoutSortCate);
        viewPager = (ViewPager)findViewById(R.id.viewPagerSortCate);
        Intent intent = getIntent();
        selCat = intent.getStringExtra("CatId");
        Title.setText("Consult "+intent.getStringExtra("CatName"));
        tabByExpert = tabLayout.newTab().setText("By Specialization");
        tabByCategories = tabLayout.newTab().setText("By Expert Name");
        tabLayout.addTab(tabByExpert);
        tabLayout.addTab(tabByCategories);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        sortPagerAdapter = new SortPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),selCat);
        viewPager.setAdapter(sortPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                sortPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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
