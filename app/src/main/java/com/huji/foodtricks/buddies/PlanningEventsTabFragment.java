package com.huji.foodtricks.buddies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.huji.foodtricks.buddies.Models.EventModel;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class PlanningEventsTabFragment extends Fragment {

    private ListView view;
    private EventListAdaptor adapter;
    private final HashMap<String, EventModel> pending_events = new HashMap<>();
    private final HashMap<String, EventModel> new_events = new HashMap<>();
    private final HashMap<String, EventModel> events_to_delete = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.planning_events, container, false);

        pending_events.putAll((HashMap<String, EventModel>) getArguments().getSerializable("events"));
        this.view = rootView.findViewById(R.id.list_view_planning);
        this.adapter = new EventListAdaptor(this.getActivity(), pending_events, false);
        this.view.setAdapter(adapter);
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.swipe_refresh_planning);
        pullToRefresh.setOnRefreshListener(() -> {
            updateEvents();
            pullToRefresh.setRefreshing(false);
        });


        return rootView;
    }

    private void updateEvents() {
        EventsTabsActivity currentActivity = (EventsTabsActivity) getActivity();
        currentActivity.resetCount();
        this.adapter.addItems(this.new_events);
        this.pending_events.putAll(new_events);
        this.new_events.clear();
        this.adapter.removeItems(this.events_to_delete);
        this.events_to_delete.clear();
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateEvents();
    }

    void addEvents(String id, EventModel event) {
        this.new_events.put(id, event);
    }

    void removeEvent(String id) {
        this.pending_events.remove(id);
    }

    void deleteEvent(String id, EventModel event) {
        this.events_to_delete.put(id, event);
    }

    void updatePressed(){
        if (adapter==null){
            return;
        }
        adapter.removePressedItem();
    }

    @Override
    public String toString() {
        return "planning";
    }
}
