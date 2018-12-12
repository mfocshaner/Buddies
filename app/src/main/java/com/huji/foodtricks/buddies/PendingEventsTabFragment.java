package com.huji.foodtricks.buddies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.huji.foodtricks.buddies.Models.EventModel;

import java.util.ArrayList;
import java.util.Date;

public class PendingEventsTabFragment extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pending_events, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.list_view_pending);
        EventListAdaptor adapter = new EventListAdaptor(this.getActivity(), getPendingEvents());
        lv.setAdapter(adapter);

        return rootView;
    }


    private ArrayList<EventModel> getPendingEvents() {
        ArrayList<EventModel> future_events = new ArrayList<>();

        EventModel event = new EventModel("pending", new Date(2018, 12, 12, 12, 30), new ArrayList<String>(), "Amit");

        future_events.add(event);
        return future_events;
    }

    @Override
    public String toString() {
        return "Pending";
    }
}
