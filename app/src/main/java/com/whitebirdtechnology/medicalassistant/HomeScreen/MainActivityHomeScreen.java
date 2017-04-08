package com.whitebirdtechnology.medicalassistant.HomeScreen;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v7.app.ActionBar;

import com.whitebirdtechnology.medicalassistant.LogInScreen.MainActivityLogInScreen;
import com.whitebirdtechnology.medicalassistant.R;
import com.whitebirdtechnology.medicalassistant.Sharepreference.ClsSharePreference;

public class MainActivityHomeScreen extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    ActionBar.LayoutParams params;
    TabLayout.Tab tabHome,tabChat,tabExpert,tabFavourite;
    TextView Title;
    ClsSharePreference clsSharePreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.action_bar_layout, null);
        params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        Title = (TextView) view.findViewById(R.id.actionbar_title);
        Title.setText("Home");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.light_blue_chat));
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.light_blue_chat));

        }

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_blue_chat)));
        getSupportActionBar().setCustomView(view,params);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME); //show custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_main_home_screen);
        clsSharePreference = new ClsSharePreference(this);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        tabHome = tabLayout.newTab().setText("Home");
        tabHome.setIcon(R.drawable.home_icon_blue);
        tabChat = tabLayout.newTab().setText("Chat");
        tabChat.setIcon(R.drawable.chat_icon_gray);
        tabExpert = tabLayout.newTab().setText("Expert");
        tabExpert.setIcon(R.drawable.expert_icon_gray);
        tabFavourite = tabLayout.newTab().setText("Favourite");
        tabFavourite.setIcon(R.drawable.fav_icon_gray);
        tabLayout.addTab(tabHome);
        tabLayout.addTab(tabChat);
        tabLayout.addTab(tabExpert);
        tabLayout.addTab(tabFavourite);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);
        viewPager.setCurrentItem(2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItemLogOut:
                clsSharePreference.ClearSharPrf();
                startActivity(new Intent(this, MainActivityLogInScreen.class));
                this.finish();
                break;
        }
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        switch (tab.getPosition()) {
            case 0:
                Title.setText("Home");
                tabHome.setIcon(R.drawable.home_icon_blue);
                tabChat.setIcon(R.drawable.chat_icon_gray);
                tabExpert.setIcon(R.drawable.expert_icon_gray);
                tabFavourite.setIcon(R.drawable.fav_icon_gray);
                break;
            case 1:
                Title.setText("Chat");
                tabHome.setIcon(R.drawable.home_icon_gray);
                tabChat.setIcon(R.drawable.chat_icon_blue);
                tabExpert.setIcon(R.drawable.expert_icon_gray);
                tabFavourite.setIcon(R.drawable.fav_icon_gray);
                break;
            case 2:
                Title.setText("Expert");
                tabHome.setIcon(R.drawable.home_icon_gray);
                tabChat.setIcon(R.drawable.chat_icon_gray);
                tabExpert.setIcon(R.drawable.expert_icon_blue);
                tabFavourite.setIcon(R.drawable.fav_icon_gray);
                break;
            case 3:
                Title.setText("Favourite");
                tabHome.setIcon(R.drawable.home_icon_gray);
                tabChat.setIcon(R.drawable.chat_icon_gray);
                tabExpert.setIcon(R.drawable.expert_icon_gray);
                tabFavourite.setIcon(R.drawable.fav_icon_blue);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
