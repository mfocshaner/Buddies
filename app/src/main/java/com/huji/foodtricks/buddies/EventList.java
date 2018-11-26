package com.huji.foodtricks.buddies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.huji.foodtricks.buddies.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.huji.foodtricks.buddies.EventModel.state.PENDING;

public class EventList extends AppCompatActivity {

    EventListAdaptor adapter;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_listview);

        lv = (ListView) findViewById(R.id.recipeListView);
        adapter = new EventListAdaptor(this, getData());
        lv.setAdapter(adapter);

    }

    private ArrayList getData() {
        ArrayList<EventModel> events_list = new ArrayList<>();
        Date time1 = new Date(2018, 11, 25);
        List<String> invitees1 = Arrays.asList("Amit", "Michael", "Aviya");
        EventModel s1 = new EventModel("dope with the guys", time1, "PENDING", invitees1);
        events_list.add(s1);

        Date time2 = new Date(2018, 12, 9);
        List<String> invitees2 = Arrays.asList("Amit", "Michael", "Nadav", "Shai", "Luna");
        EventModel s2 = new EventModel("suffering at Nadav's apartment", time1, "PENDING", invitees2);
        events_list.add(s2);

        return events_list;
    }
}