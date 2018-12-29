package com.huji.foodtricks.buddies;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.huji.foodtricks.buddies.Models.EventModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PastEventsTabFragment extends Fragment {

    View view;
    EventListAdaptor adapter;
    HashMap<String, EventModel> past_events = new HashMap<>();
    HashMap<String, EventModel> new_events = new HashMap<>();
    HashMap<String, EventModel> events_to_delete = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.past_events, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.list_view_past);
        this.adapter = new EventListAdaptor(this.getActivity(), getPastEvents());
        lv.setAdapter(this.adapter);
        updateEvents();
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.swipe_refresh_past);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateEvents();
                pullToRefresh.setRefreshing(false);
            }
        });

        return rootView;
    }


    private void updateEvents() {
        this.adapter.addItems(this.new_events);
        this.new_events.clear();
        this.adapter.removeItems(this.events_to_delete);
        this.events_to_delete.clear();
        this.adapter.notifyDataSetChanged();
    }

    private HashMap<String, EventModel> getPastEvents() {

        EventModel event = new EventModel("past", new Date(2018, 10, 10, 13, 0), new HashMap<String, String>(), "Amit");


        past_events.put("kfafsamd214u13", event);
        return past_events;
    }

    public void addEvents(String id, EventModel event) {
        this.new_events.put(id, event);
    }
    public void removeEvent(String id, EventModel event) {
        this.events_to_delete.put(id, event);
    }


    @Override
    public String toString() {
        return "Past";
    }
}
