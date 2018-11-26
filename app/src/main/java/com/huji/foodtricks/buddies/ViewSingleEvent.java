package com.huji.foodtricks.buddies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.huji.foodtricks.buddies.ui.viewsingleevent.ViewSingleEventFragment;

public class ViewSingleEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_event_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ViewSingleEventFragment.newInstance())
                    .commitNow();
        }

        Bundle prev_screen_bundle = getIntent().getExtras();
        EventModel curr_event = (EventModel)prev_screen_bundle.getSerializable("value");
    }
}
