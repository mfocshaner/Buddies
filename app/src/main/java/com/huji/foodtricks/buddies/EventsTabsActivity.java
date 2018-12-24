package com.huji.foodtricks.buddies;

import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.huji.foodtricks.buddies.Models.UserModel;


public class EventsTabsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,ViewPager.OnPageChangeListener {

    public static final String EXTRA_CURRENT_USER = "currentUser";
    public static final String EXTRA_CURRENT_UID = "currentUserID";
    ViewPager vp;
    TabLayout tabLayout;
    private UserModel currentUser;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events_tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent signedInIntent = getIntent();
        currentUser = (UserModel) signedInIntent
                .getSerializableExtra(getResources().getString(R.string.extra_current_user_model));
        currentUserID = signedInIntent
                .getStringExtra(getResources().getString(R.string.extra_current_user_id));

        setupNewEventFAB();

        vp = (ViewPager) findViewById(R.id.mViewpager_ID);
        this.addPages();

        tabLayout = (TabLayout) findViewById(R.id.mTab_ID);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(vp);
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary, getTheme()));
        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
    }

    private void setupNewEventFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.eventCreationActivityButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newEventIntent = new Intent(view.getContext(), CreateEventActivity.class);
                newEventIntent.putExtra(getResources().getString(R.string.extra_current_user_model),
                        currentUser);
                newEventIntent.putExtra(getResources().getString(R.string.extra_current_user_id),
                        currentUserID);
                startActivity(newEventIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent exit = new Intent(Intent.ACTION_MAIN);
        exit.addCategory(Intent.CATEGORY_HOME);
        exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(exit);
    }

    private void addPages()
    {
        ViewPagerAdapter pagerAdapter=new ViewPagerAdapter(this.getSupportFragmentManager());
        pagerAdapter.addFragment(new UpcomingEventsTabFragment());
        pagerAdapter.addFragment(new PendingEventsTabFragment());
        pagerAdapter.addFragment(new PastEventsTabFragment());

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