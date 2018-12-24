package com.huji.foodtricks.buddies;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.huji.foodtricks.buddies.Models.EventModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UpcomingEventsTabFragment extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.upcoming_events, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.list_view_upcoming);
        EventListAdaptor adapter = new EventListAdaptor(this.getActivity(), getUpcomingEvents());
        lv.setAdapter(adapter);

        return rootView;
    }

    private ArrayList<EventModel> getUpcomingEvents() {
        ArrayList<EventModel> upcoming_events = new ArrayList<>();

        EventModel event1 = new EventModel("brunch at Zunni's", new Date(2018, 11, 25, 10, 0), new HashMap<String, String>(), "Amit");

        upcoming_events.add(event1);

        EventModel event2 = new EventModel("kissing Data", new Date(2019, 1, 1, 0, 0), new HashMap<String, String>(), "Amit");

        upcoming_events.add(event2);
        return upcoming_events;
    }

    @Override
    public String toString() {
        return "Upcoming";
    }
}
