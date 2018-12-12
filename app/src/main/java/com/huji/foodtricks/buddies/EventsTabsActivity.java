package com.huji.foodtricks.buddies;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import android.support.v7.widget.Toolbar;


public class EventsTabsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,ViewPager.OnPageChangeListener {

    ViewPager vp;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events_tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signedInIntent = new Intent(view.getContext(), CreateEventActivity.class);
                startActivity(signedInIntent);
            }
        });

        vp = (ViewPager) findViewById(R.id.mViewpager_ID);
        this.addPages();

        tabLayout = (TabLayout) findViewById(R.id.mTab_ID);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(vp);
        tabLayout.setOnTabSelectedListener(this);

    }

    private void addPages()
    {
        ViewPagerAdapter pagerAdapter=new ViewPagerAdapter(this.getSupportFragmentManager());
        pagerAdapter.addFragment(new FutureEventsTabFragment());
        pagerAdapter.addFragment(new PastEventsTabFragment());
        pagerAdapter.addFragment(new PendingEventsTabFragment());

        vp.setAdapter(pagerAdapter);
    }

    public void onTabSelected(TabLayout.Tab tab) {
        vp.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}