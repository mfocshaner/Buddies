package com.huji.foodtricks.buddies;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.huji.foodtricks.buddies.Models.EventModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UpcomingEventsTabFragment extends Fragment {

    View view;
    EventListAdaptor adapter;
    HashMap<String, EventModel> future_events = new HashMap<>();
    HashMap<String, EventModel> new_events = new HashMap<>();

    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.upcoming_events, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.list_view_upcoming);
        this.adapter = new EventListAdaptor(this.getActivity(), getUpcomingEvents());
        lv.setAdapter(adapter);
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.swipe_refresh_future);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateEvents();
                pullToRefresh.setRefreshing(false);
            }
        });

        return rootView;
    }


    public void updateEvents() {
        this.future_events.putAll(this.new_events);
        this.new_events.clear();
        this.adapter.notifyDataSetChanged();
    }

    private ArrayList<EventModel> getUpcomingEvents() {

        EventModel event1 = new EventModel("brunch at Zunni's", new Date(2018, 11, 25, 10, 0), new HashMap<String, String>(), "Amit");

        this.future_events.put("dsfodsf34324", event1);

        return new ArrayList<EventModel>(future_events.values());
    }

    public void addEvents(String id, EventModel event) {
        this.new_events.put(id, event);
    }

    @Override
    public String toString() {
        return "Upcoming";
    }
}




