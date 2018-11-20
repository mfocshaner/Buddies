package com.huji.foodtricks.buddies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }

    /// parameters to be passed to new event
    private Date _time;
    private String _eventType; // not well defined
    private List<String> _invitees; // will maybe change if invitees become "People" objects,

    private void chooseGroup(String groupName) {
        // set invitees to be the group
    }

    private void chooseTime(Date time){
        _time = time;
    }

    private void chooseEventType(String eventType) {
        _eventType = eventType;
    }

    private String makeTitle(){
        String title = _eventType.concat("At ").concat(_time.toString());
        return title;
    }

    /**
     * Called when "create event" button is clicked
     * should be enabled only if all parameters are chosen (disable button before that)
     * @return EventModel with all chosen parameters
     */
    private EventModel createEvent(){
        String title = makeTitle();
        EventModel newEvent = new EventModel(title, _time, _eventType, _invitees);
        return newEvent;
    }

}
