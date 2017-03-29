package com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment.ListBySpecializationAndName;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment.ListBySpecializationAndName.ByExpertName.ByExpertName;
import com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment.ListBySpecializationAndName.BySpecialization.BySpecializationTab;

/**
 * Created by dell on 27/3/17.
 */

public class SortPagerAdapter extends FragmentPagerAdapter {
    int tabCount;
    String selCat;
    public SortPagerAdapter(FragmentManager fm, int tabCount,String selCat) {
        super(fm);
        this.tabCount = tabCount;
        this.selCat =selCat;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new BySpecializationTab(selCat);
            case 1:
                return new ByExpertName(selCat);
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
