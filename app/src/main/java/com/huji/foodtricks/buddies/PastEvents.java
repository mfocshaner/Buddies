package com.huji.foodtricks.buddies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class PastEvents extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.past_events, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.list_view_past);
        EventListAdaptor adapter = new EventListAdaptor(this.getActivity(), getPastEvents());
        lv.setAdapter(adapter);
        return rootView;
    }

    private ArrayList<EventModel> getPastEvents() {
        ArrayList<EventModel> future_events = new ArrayList<>();

        EventModel event = new EventModel("past", new Date(2018, 10, 10, 13, 0), new ArrayList<String>(), "Amit");

        future_events.add(event);
        return future_events;
    }

    @Override
    public String toString() {
        return "Past";
    }
}
