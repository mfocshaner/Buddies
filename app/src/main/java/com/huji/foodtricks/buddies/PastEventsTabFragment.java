package com.huji.foodtricks.buddies;

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

public class PastEventsTabFragment extends Fragment {

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
        ArrayList<EventModel> upcoming_events = new ArrayList<>();

        EventModel event = new EventModel("past", new Date(2018, 10, 10, 13, 0), new HashMap<String, String>(), "Amit");

        upcoming_events.add(event);
        return upcoming_events;
    }

    @Override
    public String toString() {
        return "Past";
    }
}
