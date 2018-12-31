package com.huji.foodtricks.buddies;

import android.content.Intent;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;


import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huji.foodtricks.buddies.Models.EventModel;
import com.huji.foodtricks.buddies.Models.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EventsTabsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    public static final String EXTRA_CURRENT_USER = "currentUser";
    public static final String EXTRA_CURRENT_UID = "currentUserID";
    ViewPager vp;
    TabLayout tabLayout;
    private UserModel currentUser;
    private String currentUserID;
    private ProgressBar spinner;

    private enum action {ADD, REMOVE}


    final DatabaseStreamer streamer = new DatabaseStreamer();
    DatabaseReference DBref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.spinner =  findViewById(R.id.progressBar1);
        //this.spinner.setVisibility(View.GONE);

        getCurrentUser();
        setDbListener();
        setContentView(R.layout.fragment_events_tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        setupNewEventFAB();
        vp = (ViewPager) findViewById(R.id.mViewpager_ID);
        this.addPages();
        this.setupNewTabLayout();
        this.firstEntry();
    }

    private void setDbListener() {
        final FirebaseDatabase DB = FirebaseDatabase.getInstance();

        DBref = DB.getReference("users" + "/" + this.currentUserID);
        DBref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot changedEvents = dataSnapshot.child("changedEvents");
                if (changedEvents.getValue() == null) {
                    return;
                }
                ArrayList<String> newEvents = (ArrayList<String>) changedEvents.getValue();
                for (final String eventId : newEvents) {
                    streamer.fetchEventModelById(eventId, new EventFetchingCompletion() {
                        @Override
                        public void onFetchSuccess(EventModel model) {
                            sendEventSwitch(eventId, model, action.ADD);
                        }

                        @Override
                        public void onNoEventFound() {
                            // cry :(
                        }
                    });
                }
                currentUser.clearChangedEvents();
                streamer.modifyUser(currentUser, currentUserID, new UserUpdateCompletion() {
                    @Override
                    public void onUpdateSuccess() {
                        return;
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        setContentView(R.layout.fragment_events_tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


    }

    private void firstEntry() {
        //this.spinner.setVisibility(View.VISIBLE);
        this.streamer.fetchEventModelsMapForCurrentUser(new EventMapFetchingCompletion() {
            @Override
            public void onFetchSuccess(HashMap<String, EventModel> modelList) {
                for (Map.Entry<String, EventModel> entry : modelList.entrySet()) {
                    sendEventSwitch(entry.getKey(), entry.getValue(), action.ADD);
                }
            }

            @Override
            public void onNoEventsFound() {
            }
        });
        //this.spinner.setVisibility(View.GONE);
    }

    private void getCurrentUser() {
        Intent signedInIntent = getIntent();
        currentUser = (UserModel) signedInIntent
                .getSerializableExtra(getResources().getString(R.string.extra_current_user_model));
        currentUserID = signedInIntent
                .getStringExtra(getResources().getString(R.string.extra_current_user_id));
    }

    private void setupNewTabLayout() {
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
        fab.setSize(FloatingActionButton.SIZE_NORMAL);
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

    public void moveEvent(Pair<String, EventModel> event, EventModel.state current_state, EventModel.state new_state) {
        sendEventSwitch(event.first, event.second, action.REMOVE);
        event.second.setEventStatus(new_state);
        this.streamer.modifyEvent(event.second, event.first, new EventUpdateCompletion() {
            @Override
            public void onUpdateSuccess() {
            }
        });
        sendEventSwitch(event.first, event.second, action.ADD);

    }

    /**
     * @param id
     * @param model
     * @param flag  false- remove event, true- add event
     */
    private void sendEventSwitch(String id, EventModel model, action flag) {
        ViewPagerAdapter adapter = (ViewPagerAdapter) vp.getAdapter();
        switch (model.getEventStatus()) {
            case UPCOMING:
                UpcomingEventsTabFragment future = (UpcomingEventsTabFragment) adapter.getItem(0);
                if (flag == action.ADD) {
                    future.addEvents(id, model);
                    return;
                }
                future.removeEvent(id, model);
                break;
            case PENDING:
                PlanningEventsTabFragment planning = (PlanningEventsTabFragment) adapter.getItem(1);
                if (flag == action.ADD) {
                    planning.addEvents(id, model);
                    return;
                }
                planning.removeEvent(id, model);
                break;
            case PAST:
                PastEventsTabFragment past = (PastEventsTabFragment) adapter.getItem(2);
                if (flag == action.ADD) {
                    past.addEvents(id, model);
                    return;
                }
                past.removeEvent(id, model);
                break;
        }
    }

    private void addPages() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        pagerAdapter.addFragment(new UpcomingEventsTabFragment());
        pagerAdapter.addFragment(new PlanningEventsTabFragment());
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