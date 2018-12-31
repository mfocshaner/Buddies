package com.huji.foodtricks.buddies;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

class ViewPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> fragments = new ArrayList<>();


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
        return fragments.get(position).toString();
    }
}
