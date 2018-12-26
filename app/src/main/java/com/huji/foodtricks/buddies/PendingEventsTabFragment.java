package com.huji.foodtricks.buddies;

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

public class PendingEventsTabFragment extends Fragment {

    View view;
    EventListAdaptor adapter;
    ArrayList<EventModel> pending_events = new ArrayList<>();
    ArrayList<EventModel> new_events = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pending_events, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.list_view_pending);
        EventListAdaptor adapter = new EventListAdaptor(this.getActivity(), getPendingEvents());
        lv.setAdapter(adapter);
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
        this.pending_events.addAll(this.new_events);
        this.adapter.notifyDataSetChanged();
    }


    private ArrayList<EventModel> getPendingEvents() {

        EventModel event = new EventModel("pending", new Date(2018, 12, 12, 12, 30), new HashMap<String, String>(), "Amit");
        pending_events.add(event);
        return pending_events;
    }

    public void addEvents(ArrayList<EventModel> events) {
        this.new_events.addAll(events);
    }

    @Override
    public String toString() {
        return "In planning";
    }
}
