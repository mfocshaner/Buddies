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

public class PastEventsTabFragment extends Fragment {

    ListView view;
    private EventListAdaptor adapter;
    private final HashMap<String, EventModel> past_events = new HashMap<>();
    private final HashMap<String, EventModel> new_events = new HashMap<>();
    private final HashMap<String, EventModel> events_to_delete = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.past_events, container, false);

        past_events.putAll((HashMap<String, EventModel>) getArguments().getSerializable("events"));
        ListView lv = (ListView) rootView.findViewById(R.id.list_view_past);
        this.adapter = new EventListAdaptor(this.getActivity(), past_events);
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.swipe_refresh_past);
        pullToRefresh.setOnRefreshListener(() -> {
            updateEvents();
            pullToRefresh.setRefreshing(false);
        });

        return rootView;
    }


    private void updateEvents() {
        EventsTabsActivity currentActivity = (EventsTabsActivity) getActivity();
        currentActivity.reduceCount(this.new_events.size() + this.events_to_delete.size());
        this.adapter.addItems(this.new_events);
        this.past_events.putAll(new_events);
        this.new_events.clear();
        this.adapter.removeItems(this.events_to_delete);
        this.events_to_delete.clear();
        this.adapter.notifyDataSetChanged();
    }


    void addEvents(String id, EventModel event) {
        this.new_events.put(id, event);
    }

    public void removeEvent(String id) {
        this.past_events.remove(id);
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
        return "Past";
    }
}
