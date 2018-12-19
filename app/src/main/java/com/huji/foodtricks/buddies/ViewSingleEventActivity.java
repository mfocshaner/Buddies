package com.huji.foodtricks.buddies;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.huji.foodtricks.buddies.Models.EventModel;
import com.huji.foodtricks.buddies.Models.UserModel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class ViewSingleEventActivity extends AppCompatActivity {

    static EventModel curr_event;
    static UserModel curr_user;
    private DatabaseStreamer _dbs = new DatabaseStreamer();
    String curr_event_id = "ABCDEFG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent eventCard = getIntent();
        curr_event = (EventModel) eventCard.getSerializableExtra("event");
//        final String curr_event_id = eventCard.getStringExtra("event_id");
        curr_event_id = "ABCDEFG";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_event);

        Objects.requireNonNull(getSupportActionBar()).setTitle(curr_event.getTitle());
        updateAllFields(curr_event);
    }

    private void updateAllFields(EventModel curr_event) {
        TextView who_tv = (TextView) findViewById(R.id.who_text);
        who_tv.setText(curr_event.getTitle());
        TextView when_tv = (TextView) findViewById(R.id.when_textView);
        when_tv.setText("Tomorrow night");
        TextView what_tv = (TextView) findViewById(R.id.what_text);
        what_tv.setText(curr_event.getTitle());
        MapView location_mv = (MapView) findViewById(R.id.mapView);


    }

    public void onButtonShowPopupWindowClick(View view) {

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_user_list_, null);
        // create the popup window
        PopupWindow pw = new PopupWindow(popupView);
        TextView tv = pw.getContentView().findViewById(R.id.users_list);
        switch (view.getId()) {
            case R.id.who_is_coming_btn:
                tv.setText(String.join("\n", curr_event.getAttendanceProvider().getAttending()));
                break;
            case R.id.who_is_tentative:
                tv.setText(String.join("\n", curr_event.getAttendanceProvider().getTentatives()));
                break;
            case R.id.who_isnt_coming_btn:
                tv.setText(String.join("\n", curr_event.getAttendanceProvider().getNotAttending()));
                break;

        }
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void discard_event_click(View view) {
    }

    public void onRSVPChangeClick(View view) {
//        Toast.makeText(ViewSingleEventActivity.this, getString(R.string.change_rsvp_msg_prefix) + .getTitle(), Toast.LENGTH_SHORT).show();
        EventAttendanceProvider attendanceProvider = curr_event.getAttendanceProvider();
        String uId;
        try {
            uId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Should use getIntent().getSerializable
        } catch (NullPointerException e) {
            uId = "Null";
        }

        if (view.getId() == R.id.approve_btn) {
            attendanceProvider.markAttending(uId);

        } else if (view.getId() == R.id.tentative_btn) {
            attendanceProvider.markTentative(uId);
        } else if (view.getId() == R.id.decline_btn) {
            attendanceProvider.markNotAttending(uId);
        }
        _dbs.modifyEvent(curr_event, curr_event_id, new EventUpdateCompletion() {
            @Override
            public void onResponse() {
                Toast update_event_updated = Toast.makeText(getApplicationContext(),
                        "Update completed", Toast.LENGTH_SHORT);
                update_event_updated.show();
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
        Set<Integer> allRSVPButtons = new HashSet<>(Arrays.asList(R.id.approve_btn, R.id.tentative_btn, R.id.decline_btn));
        allRSVPButtons.remove(view.getId()); // remove the selected button from the list of buttons to disable
        Button selectedButtonView = (Button) view; // just for readability
        selectedButtonView.setBackgroundColor(Color.MAGENTA);
        selectedButtonView.setTextColor(Color.WHITE);
//        setContentView(R.layout.activity_view_single_event);
        for(int buttonId:allRSVPButtons) {
           Button currButton =  findViewById(buttonId);
           currButton.setBackgroundColor(Color.WHITE);
           currButton.setTextColor(Color.BLACK);
        }
    }
}
