package com.huji.foodtricks.buddies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
//
//        Bundle prev_screen_bundle = getIntent().getExtras();
//        EventModel curr_event = (EventModel)prev_screen_bundle.getSerializable("curr_event");
//        TextView who_tv = (TextView)findViewById(R.id.who_text);
//        who_tv.setText(curr_event.getEventType());
////        TextView when_tv = (TextView)findViewById(R.id.when_text);
////        who_tv.setText(curr_event.getEventType());
//        TextView what_tv = (TextView)findViewById(R.id.what_text);
//        what_tv.setText(curr_event.getEventType());

        // change_title to event description
        // init map and
        // change map aim to location




    }
}
