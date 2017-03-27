package com.whitebirdtechnology.medicalassistant.HomeScreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.whitebirdtechnology.medicalassistant.ChatFragment.ChatTab;
import com.whitebirdtechnology.medicalassistant.ExpertFragment.ExpertTab;
import com.whitebirdtechnology.medicalassistant.FavouriteFragment.FavouriteTab;
import com.whitebirdtechnology.medicalassistant.HomeFragment.HomeTab;

/**
 * Created by dell on 22/3/17.
 */

public class PagerAdapter extends FragmentPagerAdapter {
        int tabCount;
    public PagerAdapter(FragmentManager fragmentManager,int tabCount){
        super(fragmentManager);
        this.tabCount = tabCount;

    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeTab();
            case 1:
                return new ChatTab();
            case 2:
                return new ExpertTab();
            case 3:
                return new FavouriteTab();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
