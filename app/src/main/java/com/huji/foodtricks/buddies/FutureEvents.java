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

        EventModel event1 = new EventModel("brunch at Zunni's", new Date(2018, 11, 25, 10, 0), new ArrayList<String>(), "Amit");

        future_events.add(event1);

        EventModel event2 = new EventModel("kissing Data", new Date(2019, 1, 1, 0, 0), new ArrayList<String>(), "Amit");

        future_events.add(event2);
        return future_events;
    }

    @Override
    public String toString() {
        return "Future";
    }
}
