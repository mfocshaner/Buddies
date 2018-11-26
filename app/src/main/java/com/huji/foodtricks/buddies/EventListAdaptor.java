package com.huji.foodtricks.buddies;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class EventListAdaptor extends BaseAdapter {
    Context c;
    ArrayList<EventModel> events;

    public EventListAdaptor(Context c, ArrayList<EventModel> spacecrafts) {
        this.c = c;
        this.events = spacecrafts;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int i) {
        return events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
//            view= LayoutInflater.from(c).inflate(R.layout.event_cardview,viewGroup,false);
        }

        final EventModel s= (EventModel) this.getItem(i);

//        ImageView img= (ImageView) view.findViewById(R.id.event_image);
//        TextView nameTxt= (TextView) view.findViewById(R.id.event_title);

        //img.setImageResource(s.getImage());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, s.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}