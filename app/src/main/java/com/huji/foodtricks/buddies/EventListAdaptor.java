package com.huji.foodtricks.buddies;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.huji.foodtricks.buddies.Models.EventModel;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

class EventListAdaptor extends BaseAdapter {
    private final Context context;
    private final HashMap<String, EventModel> eventModels;
    private final HashMap<String, EventModel> pressedToDelete = new HashMap<>();
    private final LinkedList<Map.Entry<String, Date>> keysByDate;

    public EventListAdaptor(Context context, HashMap<String, EventModel> eventModels,
                            boolean isPast) {
        this.context = context;
        this.eventModels = eventModels;
        this.keysByDate = getKeysSortedByEventDates(eventModels, isPast);
    }

    public LinkedList<Map.Entry<String, Date>> getKeysSortedByEventDates(HashMap<String,
            EventModel> eventModels, boolean reverseOrder) {
        HashMap<String, Date> eventsDates = new HashMap<>();
        for (String eventKey : eventModels.keySet()) {
            eventsDates.put(eventKey, eventModels.get(eventKey).getTime());
        }

        LinkedList<Map.Entry<String, Date>> eventsDatesList = new LinkedList<>(eventsDates.entrySet());

        eventsDatesList.sort(new Comparator<Map.Entry<String, Date>>() {
            public int compare(Map.Entry<String, Date> o1, Map.Entry<String, Date> o2) {
                if (reverseOrder) {
                    return o2.getValue().compareTo(o1.getValue());
                }
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        return eventsDatesList;
    }

    public void addItems(HashMap<String, EventModel> events) {
        eventModels.putAll(events);
        HashMap<String, Date> eventsDates = new HashMap<>();
        for (String eventKey : events.keySet()) {
            eventsDates.put(eventKey, events.get(eventKey).getTime());
        }
        // add new events in the start of the list because I assume the user would like to see them
        // at the top.
        this.keysByDate.removeAll(eventsDates.entrySet());
        this.keysByDate.addAll(0, new LinkedList<>(eventsDates.entrySet()));
    }

    public void removeItems(HashMap<String, EventModel> events) {
        eventModels.keySet().removeAll(events.keySet());
        for (String eventKey : events.keySet()) {
            for (Map.Entry<String, Date> entry: this.keysByDate){
                if (entry.getKey().equals(eventKey)) {
                    this.keysByDate.remove(entry);
                    break;
                }
            }
        }
    }

    public void removePressedItem() {
        if (pressedToDelete.isEmpty()) {
            return;
        }
        removeItems(this.pressedToDelete);
        this.pressedToDelete.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return eventModels.size();
    }

    @Override
    public Object getItem(int i) {
        String eventKey = this.keysByDate.get(i).getKey();
        return eventModels.get(eventKey);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.event_card, viewGroup, false);
        }

        final EventModel event = (EventModel) this.getItem(i);
        final String key = this.keysByDate.get(i).getKey();;


        TextView eventName = view.findViewById(R.id.eventCardName);
        TextView eventDate = view.findViewById(R.id.eventCardDate);
        ImageView eventImage = view.findViewById(R.id.eventCardImage);
        ImageView deleteButton = view.findViewById(R.id.delete);
        deleteButton.setVisibility(View.GONE);
        final String name = event.getTitle();
        Calendar dateTime = new GregorianCalendar();
        dateTime.setTime(event.getTime());
        String dateTimeString = MessageFormat.format("{4} {0}/{1} {2}:{3}",
                dateTime.get(Calendar.DATE),
                dateTime.get(Calendar.MONTH) + 1,
                dateTime.get(Calendar.HOUR_OF_DAY),
                String.format(Locale.getDefault(), "%02d", dateTime.get(Calendar.MINUTE)),
                dateTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));

        eventName.setText(name);
        eventDate.setText(dateTimeString);
        GlideApp.with(context)
                .load(event.getImageUrl())
                .apply(RequestOptions.circleCropTransform())
                .override(300, 300)
                .into(eventImage);


        view.setOnClickListener(view1 -> {
            Context context = view1.getContext();
            Intent viewEventIntent = new Intent(context, ViewSingleEventActivity.class);
            viewEventIntent.putExtra(context.getResources()
                    .getString(R.string.extra_current_event_model), event);
            viewEventIntent.putExtra(context.getResources()
                    .getString(R.string.extra_current_event_id), key);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context);
            context.startActivity(viewEventIntent, options.toBundle());
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ImageView deleteButton = v.findViewById(R.id.delete);
                deleteButton.setVisibility(View.VISIBLE);
                pressedToDelete.put(key, event);
                return true;
            }
        });

        return view;
    }
}