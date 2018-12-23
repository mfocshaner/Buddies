package com.huji.foodtricks.buddies;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    public void addFragment(Fragment f) {
        fragments.add(f);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = fragments.get(position).toString();
        return title;
    }
}
