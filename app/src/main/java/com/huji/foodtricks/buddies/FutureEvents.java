package com.huji.foodtricks.buddies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FutureEvents extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.future_events, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.list_view_future);
        EventListAdaptor adapter = new EventListAdaptor(this.getActivity(), getFutureEvents());
        lv.setAdapter(adapter);

        return rootView;
    }

    private ArrayList<EventModel> getFutureEvents() {
        ArrayList<EventModel> future_events = new ArrayList<>();

        EventModel event = new EventModel("future", new Date(2018, 12, 1), "future", new ArrayList<String>());

        future_events.add(event);
        return future_events;
    }

    @Override
    public String toString() {
        return "Future";
    }
}
