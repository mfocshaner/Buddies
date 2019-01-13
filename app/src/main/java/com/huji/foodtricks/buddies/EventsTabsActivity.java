package com.huji.foodtricks.buddies;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


public class EventsTabsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    public static final String EXTRA_CURRENT_USER = "currentUser";
    public static final String EXTRA_CURRENT_UID = "currentUserID";
    private ViewPager vp;
    private TabLayout tabLayout;
    private UserModel currentUser;
    private String currentUserID;


    private enum action {ADD, REMOVE;}

    private final DatabaseStreamer streamer = new DatabaseStreamer();

    private DatabaseReference DBref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.spinner =  findViewById(R.id.progressBar1);
        //this.spinner.setVisibility(View.GONE);

        getCurrentUser();
        setDbListener();
        setContentView(R.layout.fragment_events_tabs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        setupNewEventFAB();
        vp = findViewById(R.id.mViewpager_ID);
        this.addPages();
        this.setupNewTabLayout();
        this.firstEntry();
    }

    private void setDbListener() {
        final FirebaseDatabase DB = FirebaseDB.getDatabase();

        DBref = DB.getReference("users" + "/" + this.currentUserID);
        DBref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel updateUserModel = dataSnapshot.getValue(UserModel.class);
                if (updateUserModel == null) {
                    return;
                }
                currentUser = updateUserModel;
                if (currentUser.getChangedEvents() == null ||
                        currentUser.getChangedEvents().size() == 0) {
                    return;
                }
                ArrayList<String> newEvents = currentUser.getChangedEvents();
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
                streamer.clearUsersChangedEvents(currentUserID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        setContentView(R.layout.fragment_events_tabs);
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        tabLayout = findViewById(R.id.mTab_ID);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(vp);
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary, getTheme()));
        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
    }

    private void setupNewEventFAB() {
        FloatingActionButton fab = findViewById(R.id.eventCreationActivityButton);
        fab.setSize(FloatingActionButton.SIZE_NORMAL);
        fab.setOnClickListener(view -> {
            Intent newEventIntent = new Intent(view.getContext(), CreateEventActivity.class);
            newEventIntent.putExtra(getResources().getString(R.string.extra_current_user_model),
                    currentUser);
            newEventIntent.putExtra(getResources().getString(R.string.extra_current_user_id),
                    currentUserID);

            EventsTabsActivity context= EventsTabsActivity.this;
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(context);
            startActivity(newEventIntent, options.toBundle());
        });
    }

    @Override
    public void onBackPressed() {
        Intent exit = new Intent(Intent.ACTION_MAIN);
        exit.addCategory(Intent.CATEGORY_HOME);
        exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(exit);
    }

    // todo: Amit to decide whether this is needed

    public void moveEvent(Pair<String, EventModel> event, EventModel.state current_state, EventModel.state new_state) {
        sendEventSwitch(event.first, event.second, action.REMOVE);
        event.second.setEventStatus(new_state);
        this.streamer.modifyEvent(event.second, event.first, () -> {
        });
        sendEventSwitch(event.first, event.second, action.ADD);

    }

    private void sendEventSwitch(String id, EventModel model, action flag) {
        ViewPagerAdapter adapter = (ViewPagerAdapter) vp.getAdapter();
        if (adapter == null) {
            return;
        }
        UpcomingEventsTabFragment future = (UpcomingEventsTabFragment) adapter.getItem(0);
        PastEventsTabFragment past = (PastEventsTabFragment) adapter.getItem(1);

        switch (model.getEventStatus()) {
            case UPCOMING:
                if (flag == action.ADD) {
                    future.addEvents(id, model);
                    return;
                }
                future.deleteEvent(id, model);
                break;
            case PAST:
                if (flag == action.ADD) {
                    past.addEvents(id, model);
                    future.removeEvent(id);
                    return;
                }
                past.deleteEvent(id, model);
                break;
        }
    }

    private void addPages() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        pagerAdapter.addFragment(new UpcomingEventsTabFragment());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.events_tabs_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signoutButton:
                setResult(SignInActivity.RC_SIGN_OUT);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}