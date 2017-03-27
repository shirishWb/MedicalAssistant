package com.whitebirdtechnology.medicalassistant.ExpertFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whitebirdtechnology.medicalassistant.R;

/**
 * Created by dell on 22/3/17.
 */

public class ExpertTab extends Fragment implements TabLayout.OnTabSelectedListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    TabLayout.Tab tabByExpert,tabByCategories;
    ExpertPagerAdapter expertPagerAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expert_tab,container,false);
        tabLayout = (TabLayout)view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager)view.findViewById(R.id.viewPagerExpert);

        tabByExpert = tabLayout.newTab().setText("By Expert");
        tabByCategories = tabLayout.newTab().setText("By Categories");
        tabLayout.addTab(tabByExpert);
        tabLayout.addTab(tabByCategories);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        expertPagerAdapter = new ExpertPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(expertPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                expertPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setOnTabSelectedListener(this);
        return view;
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
}
