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

        future_events.putAll((HashMap<String, EventModel>) getArguments().getSerializable("events"));
        ListView lv = rootView.findViewById(R.id.list_view_upcoming);
        this.adapter = new EventListAdaptor(this.getActivity(), future_events);
        lv.setAdapter(adapter);
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.swipe_refresh_future);
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
        this.future_events.putAll(new_events);
        this.new_events.clear();
        this.adapter.removeItems(this.events_to_delete);
        this.events_to_delete.clear();
        this.adapter.notifyDataSetChanged();
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

    public void updatePressed(){
        if (adapter==null){
            return;
        }
        adapter.removePressedItem();
    }

    @Override
    public String toString() {
        return "Upcoming";
    }
}




