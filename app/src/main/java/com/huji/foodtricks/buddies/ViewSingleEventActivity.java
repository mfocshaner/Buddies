package com.huji.foodtricks.buddies;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.huji.foodtricks.buddies.Models.EventModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;


public class ViewSingleEventActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final HashSet<Integer> ALL_RSVP_BUTTONS =
            new HashSet<>(Arrays.asList(R.id.approve_btn, R.id.tentative_btn, R.id.decline_btn));
    private static EventModel curr_event;
    private String currentUserID;
    private final DatabaseStreamer dbs = new DatabaseStreamer();
    private String curr_event_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent eventCard = getIntent();
        curr_event = (EventModel) eventCard.getSerializableExtra(getResources().getString(R.string.extra_current_event_model));
        curr_event_id = eventCard.getStringExtra(getResources().getString(R.string.extra_current_event_id));
        currentUserID = FirebaseAuth.getInstance().getUid();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_event);

        Objects.requireNonNull(getSupportActionBar()).setTitle(curr_event.getTitle());
        updateAllFields(curr_event);
        RSVPListAdapter.setupUserList(this, curr_event.getAttendanceProvider());

        // there is no error - it is a known issue : https://stackoverflow.com/questions/51179459/supportmapfragment-does-not-support-androidx-fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.g_map);
        mapFragment.getMapAsync(this);

    }

    private void updateAllFields(EventModel curr_event) {
        TextView date_tv = findViewById(R.id.date_textView);
        date_tv.setText(curr_event.getTime().toString());
        modifyDateTextView(curr_event.getTime(), date_tv);

        TextView hour_tv = findViewById(R.id.hour_textView);
        modifyHourTextView(curr_event.getTime(), hour_tv);
        RSVPListAdapter.setupUserList(this, curr_event.getAttendanceProvider());
        modifyRSVPButtons();
    }


    private void modifyDateTextView(Date time, TextView date_tv) {
        // TODO: use a Calendar to parse date and hour elements instead, and then catching will be unneeded
        DateFormat formatter = new SimpleDateFormat("dd/MM", Locale.getDefault());
        try {
            time = formatter.parse(formatter.format(time));
        } catch (ParseException e) {
            date_tv.setText(R.string.invalid_date_error_text);
        }
        date_tv.setText(formatter.format(time));

    }

    private void modifyHourTextView(Date time, TextView hour_tv) {
        Formatter fmt = new Formatter();
        fmt.format("%tl:%tM", time, time);
        hour_tv.setText(fmt.toString());
    }


    public void onRSVPChangeClick(View view) {
        EventAttendanceProvider attendanceProvider = curr_event.getAttendanceProvider();
        if (currentUserID == null)
            return;

        if (view.getId() == R.id.approve_btn) {
            attendanceProvider.markAttending(currentUserID);
        } else if (view.getId() == R.id.tentative_btn) {
            attendanceProvider.markTentative(currentUserID);
        } else if (view.getId() == R.id.decline_btn) {
            attendanceProvider.markNotAttending(currentUserID);
        }
        curr_event.setAttendanceProvider(attendanceProvider);
        dbs.modifyEvent(curr_event, curr_event_id, () -> {
        });
        modifyRSVPButtons();
    }

    private void modifyRSVPButtons() {
        Set<Integer> allRSVPButtons = new HashSet<>(ALL_RSVP_BUTTONS);
        for (int buttonId : allRSVPButtons) {
            Button currButton = findViewById(buttonId);
            changeButtonToDisabled(currButton);
        }
        RSVPListAdapter.setupUserList(this, curr_event.getAttendanceProvider());
        EventAttendanceProvider attendanceProvider = curr_event.getAttendanceProvider();
        EventAttendanceProvider.RSVP status = attendanceProvider.getUserRSVP(currentUserID);
        if (status == EventAttendanceProvider.RSVP.ATTENDING) {
            changeButtonToEnabled(findViewById(R.id.approve_btn));
        } else if (status == EventAttendanceProvider.RSVP.NOT_ATTENDING)
            changeButtonToEnabled(findViewById(R.id.decline_btn));
        else if (status == EventAttendanceProvider.RSVP.TENTATIVE)
            changeButtonToEnabled(findViewById(R.id.tentative_btn));
    }

    private void changeButtonToEnabled(Button selectedButtonView) {
        selectedButtonView.setBackgroundColor(getResources().getColor(R.color.selectedRSVPButton, getTheme()));
        selectedButtonView.setTextColor(Color.WHITE);
        selectedButtonView.setClickable(false);
    }

    private void changeButtonToDisabled(Button currButton) {
        currButton.setBackgroundColor(Color.WHITE);
        currButton.setTextColor(Color.BLACK);
        currButton.setClickable(true);

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //seattle coordinates
            LatLng seattle = new LatLng(47.6062095, -122.3320708);
        mMap.addMarker(new MarkerOptions().position(seattle).title("Seattle"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seattle));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
    }


}


