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

public class PendingEventsTabFragment extends Fragment {

    ListView view;
    EventListAdaptor adapter;
    HashMap<String, EventModel> pending_events = new HashMap<>();
    HashMap<String, EventModel> new_events = new HashMap<>();
    HashMap<String, EventModel> events_to_delete = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pending_events, container, false);

        this.view = (ListView) rootView.findViewById(R.id.list_view_pending);
        this.adapter = new EventListAdaptor(this.getActivity(), getPendingEvents());
        this.view.setAdapter(adapter);
        updateEvents();
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.swipe_refresh_pending);
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


    private HashMap<String, EventModel> getPendingEvents() {

        EventModel event = new EventModel("pending", new Date(2018, 12, 12, 12, 30), new HashMap<String, String>(), "Amit");
        pending_events.put("afkaflkaflkma13", event);
        return pending_events;
    }

    public void addEvents(String id, EventModel event) {
        this.new_events.put(id, event);
    }

    public void removeEvent(String id, EventModel event) {
        this.events_to_delete.put(id, event);
    }

    @Override
    public String toString() {
        return "In planning";
    }
}
