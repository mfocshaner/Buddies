package com.huji.foodtricks.buddies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.huji.foodtricks.buddies.Models.EventModel;
import com.huji.foodtricks.buddies.Models.UserModel;

import java.util.Objects;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class ViewSingleEventActivity extends AppCompatActivity {

    static EventModel curr_event;
    static UserModel curr_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent eventCard = getIntent();
        curr_event = (EventModel) eventCard.getSerializableExtra("event");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_event);

        Objects.requireNonNull(getSupportActionBar()).setTitle(curr_event.getTitle());
        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fabSpeedDial);
        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                Toast.makeText(ViewSingleEventActivity.this, getString(R.string.change_rsvp_msg_prefix) + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                EventAttendanceProvider attendanceProvider = curr_event.getAttendanceProvider();
                if (menuItem.getTitle() == getString(R.string.approve_msg)) {
                    attendanceProvider.markAttending(curr_user.getUserAuthenticationId());
                } else if (menuItem.getTitle() == getString(R.string.tentative_msg)) {
                    attendanceProvider.markTentative(curr_user.getUserAuthenticationId());
                } else if (menuItem.getTitle() == getString(R.string.decline_msg)) {
                    attendanceProvider.markNotAttending(curr_user.getUserAuthenticationId());
                }
                return true;
            }

            @Override
            public void onMenuClosed() {

            }
        });
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
        switch (view.getId())
        {
            case R.id.who_is_coming_btn:
                tv.setText(String.join("\n",curr_event.getAttendanceProvider().getAttending()));
                break;
            case R.id.who_is_tentative:
                tv.setText(String.join("\n",curr_event.getAttendanceProvider().getTentatives()));
                break;
            case R.id.who_isnt_coming_btn:
                tv.setText(String.join("\n",curr_event.getAttendanceProvider().getNotAttending()));
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
}
