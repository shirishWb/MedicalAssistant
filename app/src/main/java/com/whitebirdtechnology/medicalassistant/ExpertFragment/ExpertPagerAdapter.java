package com.whitebirdtechnology.medicalassistant.ExpertFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment.ByCategoriesTab;
import com.whitebirdtechnology.medicalassistant.ExpertFragment.ByExpertFragment.ByExpertTab;
import com.whitebirdtechnology.medicalassistant.ExpertFragment.ByExpertFragment.SingListByExpert;

/**
 * Created by dell on 23/3/17.
 */

public class ExpertPagerAdapter extends FragmentPagerAdapter  {
    int tabCount;
    public ExpertPagerAdapter(FragmentManager fm,int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ByExpertTab();
            case 1:
                return new ByCategoriesTab();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
