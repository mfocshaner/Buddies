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

class UpcomingEventsTabFragment extends Fragment {

    ListView view;
    private EventListAdaptor adapter;
    private final HashMap<String, EventModel> future_events = new HashMap<>();
    private final HashMap<String, EventModel> new_events = new HashMap<>();
    private final HashMap<String, EventModel> events_to_delete = new HashMap<>();

    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.upcoming_events, container, false);

        ListView lv = rootView.findViewById(R.id.list_view_upcoming);
        this.adapter = new EventListAdaptor(this.getActivity(), getUpcomingEvents());
        lv.setAdapter(adapter);
        updateEvents();
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.swipe_refresh_future);
        pullToRefresh.setOnRefreshListener(() -> {
            updateEvents();
            pullToRefresh.setRefreshing(false);
        });

        return rootView;
    }

    public void updateEvents() {
        this.adapter.addItems(this.new_events);
        this.future_events.putAll(new_events);
        this.new_events.clear();
        this.adapter.removeItems(this.events_to_delete);
        this.events_to_delete.clear();
        this.adapter.notifyDataSetChanged();
    }

    private HashMap<String, EventModel> getUpcomingEvents() {

//        EventModel event1 = new EventModel("brunch at Zunni's", new GregorianCalendar(2018, 11, 25, 10, 0).getTime(), new HashMap<>(), "Amit", "https://lh5.googleusercontent.com/-IL-Nkaz5E1s/AAAAAAAAAAI/AAAAAAAAABA/hQWtV0XNRrw/s96-c/photo.jpg");
//
//        this.future_events.put("dsfodsf34324", event1);

        return future_events;
    }

    public void addEvents(String id, EventModel event) {
        this.new_events.put(id, event);
    }

    public void removeEvent(String id) {
        this.future_events.remove(id);
    }

    public void deleteEvent(String id, EventModel event) {
        this.events_to_delete.put(id, event);
    }

    @Override
    public String toString() {
        return "Upcoming";
    }
}




