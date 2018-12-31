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

class PastEventsTabFragment extends Fragment {

    View view;
    private EventListAdaptor adapter;
    private final HashMap<String, EventModel> past_events = new HashMap<>();
    private final HashMap<String, EventModel> new_events = new HashMap<>();
    private final HashMap<String, EventModel> events_to_delete = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.past_events, container, false);

        ListView lv = rootView.findViewById(R.id.list_view_past);
        this.adapter = new EventListAdaptor(this.getActivity(), getPastEvents());
        lv.setAdapter(this.adapter);
        updateEvents();
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.swipe_refresh_past);
        pullToRefresh.setOnRefreshListener(() -> {
            updateEvents();
            pullToRefresh.setRefreshing(false);
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

        EventModel event = new EventModel("past", new GregorianCalendar(2018, 0, 10, 13, 0).getTime(), new HashMap<>(), "Amit", "https://lh5.googleusercontent.com/-IL-Nkaz5E1s/AAAAAAAAAAI/AAAAAAAAABA/hQWtV0XNRrw/s96-c/photo.jpg");


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
