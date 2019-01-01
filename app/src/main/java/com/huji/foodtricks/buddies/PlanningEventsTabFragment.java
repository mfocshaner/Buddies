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

import java.util.GregorianCalendar;
import java.util.HashMap;

class PlanningEventsTabFragment extends Fragment {

    private ListView view;
    private EventListAdaptor adapter;
    private final HashMap<String, EventModel> pending_events = new HashMap<>();
    private final HashMap<String, EventModel> new_events = new HashMap<>();
    private final HashMap<String, EventModel> events_to_delete = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.planning_events, container, false);

        this.view = rootView.findViewById(R.id.list_view_planning);
        this.adapter = new EventListAdaptor(this.getActivity(), getPendingEvents());
        this.view.setAdapter(adapter);
        updateEvents();
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.swipe_refresh_planning);
        pullToRefresh.setOnRefreshListener(() -> {
            updateEvents();
            pullToRefresh.setRefreshing(false);
        });


        return rootView;
    }

    private void updateEvents() {
        this.adapter.addItems(this.new_events);
        this.pending_events.putAll(new_events);
        this.new_events.clear();
        this.adapter.removeItems(this.events_to_delete);
        this.events_to_delete.clear();
        this.adapter.notifyDataSetChanged();
    }


    private HashMap<String, EventModel> getPendingEvents() {

        EventModel event = new EventModel("planning", new GregorianCalendar(2018, 10, 12, 12, 30).getTime(), new HashMap<>(), "Amit", "https://lh5.googleusercontent.com/-IL-Nkaz5E1s/AAAAAAAAAAI/AAAAAAAAABA/hQWtV0XNRrw/s96-c/photo.jpg");
        pending_events.put("afkaflkaflkma13", event);
        return pending_events;
    }

    public void addEvents(String id, EventModel event) {
        this.new_events.put(id, event);
    }

    public void removeEvent(String id) {
        this.pending_events.remove(id);
    }

    public void deleteEvent(String id, EventModel event) {
        this.events_to_delete.put(id, event);
    }

    @Override
    public String toString() {
        return "planning";
    }
}
