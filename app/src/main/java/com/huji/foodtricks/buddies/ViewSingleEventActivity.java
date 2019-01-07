package com.huji.foodtricks.buddies;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.huji.foodtricks.buddies.Models.EventModel;
import com.huji.foodtricks.buddies.Models.UserModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;


public class ViewSingleEventActivity extends AppCompatActivity {

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


    }

    private void updateAllFields(EventModel curr_event) {


        TextView date_tv = findViewById(R.id.date_textView);
        date_tv.setText(curr_event.getTime().toString());
        modifyDateTextView(curr_event.getTime(), date_tv);

        TextView hour_tv = findViewById(R.id.hour_textView);
        modifyHourTextView(curr_event.getTime(), hour_tv);
        setupUserList();
        modifyRSVPButtons();

        if (!curr_event.isUserOrganizer(currentUserID) || curr_event.getEventStatus() != EventModel.state.PENDING)
        {
            FloatingActionButton discard_btn = findViewById(R.id.discard_event);
            FloatingActionButton approve_btn= findViewById(R.id.approve_event);
            discard_btn.setVisibility(View.GONE);
            approve_btn.setVisibility(View.GONE);
        }

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
            Toast update_event_updated = Toast.makeText(getApplicationContext(),
                    "Update completed", Toast.LENGTH_SHORT);
            update_event_updated.show();
            // we'd maybe want to notify all users that there's something new about this event.
        });
        modifyRSVPButtons();
    }

    private void setupRSVPList(LinearLayout containingLayout, HashMap<String, UserModel> usersMap) {
        if(containingLayout.getChildCount() > 0) {
            containingLayout.removeAllViews();
        }
        for (String userId : usersMap.keySet()) {
            if (curr_event.getAttendanceProvider().getInvitees().containsKey(userId)) {
                final LinearLayout rowLinearLayout = new LinearLayout(this);
                rowLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                TextView userNameTextView = new TextView(this);
                UserModel userModel = usersMap.get(userId);
                userNameTextView.setText(Objects.requireNonNull(userModel).getUserName());
                userNameTextView.setTextSize(20);
                userNameTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                int btnRSVP = getUserRSVPButton(userId);
                ImageView rsvpImage = new ImageView(this);
                rsvpImage.setImageResource(btnRSVP);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.END);
                rsvpImage.setLayoutParams(layoutParams);
                ImageView userImage = new ImageView(this);
                GlideApp.with(this)
                        .load(Objects.requireNonNull(userModel).getImageUrl())
                        .override(100, 100)
                        .apply(RequestOptions.circleCropTransform())
                        .into(userImage);
                rowLinearLayout.addView(userImage);
                rowLinearLayout.addView(userNameTextView);
                rowLinearLayout.addView(rsvpImage);
                rowLinearLayout.setPadding(10, 10, 10, 20);
                containingLayout.addView(rowLinearLayout);
            }
        }
    }

    private int getUserRSVPButton(String userId) {
        EventAttendanceProvider.RSVP rsvpAnswer = curr_event.getAttendanceProvider().getUserRSVP(userId);
        switch (rsvpAnswer)
        {
            case ATTENDING:
                return R.drawable.going_rsvp_list_icon;
            case NOT_ATTENDING:
                return R.drawable.not_going_rsvp_list_icon;
        }
        return R.drawable.maybe_rsvp_list_icon;
    }

    private void setupUserList() {
        final LinearLayout linearLayout = findViewById(R.id.linearLayoutForUserRSVP);

        getUsersFromDB(new UsersMapFetchingCompletion() {
            @Override
            public void onFetchSuccess(HashMap<String, UserModel> usersMap) {
                setupRSVPList(linearLayout, usersMap);
            }

            @Override
            public void onNoUsersFound() {
                // TODO: show that there are no users?
            }
        });
    }

    private void getUsersFromDB(UsersMapFetchingCompletion completion) {
        DatabaseStreamer dbs = new DatabaseStreamer();
        dbs.fetchAllUsersInDBMap(completion);
    }

    private void modifyRSVPButtons() {
        setupUserList();
        Set<Integer> allRSVPButtons = new HashSet<>(ALL_RSVP_BUTTONS);
        for (int buttonId : allRSVPButtons) {
            Button currButton = findViewById(buttonId);
            changeButtonToDisabled(currButton);
        }
//        TextView rsvp_tv = findViewById(R.id.RSVPText);
//        modifyAttendersTextView(curr_event.getAttendanceProvider(), rsvp_tv);
        EventAttendanceProvider attendanceProvider = curr_event.getAttendanceProvider();
        EventAttendanceProvider.RSVP status = attendanceProvider.getUserRSVP(currentUserID);
        if (status == EventAttendanceProvider.RSVP.ATTENDING) {
            changeButtonToEnabled(findViewById(R.id.approve_btn));
        }
        else if (status == EventAttendanceProvider.RSVP.NOT_ATTENDING)
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

    public void approve_event(View view) {
        curr_event.setEventStatus(EventModel.state.UPCOMING);
        dbs.modifyEvent(curr_event, curr_event_id, () -> {

        });
        updateAllFields(curr_event);
    }

    public void discard_event(View view) {
        curr_event.setEventStatus(EventModel.state.DELETED);
        dbs.modifyEvent(curr_event, curr_event_id, () -> {

        });
        updateAllFields(curr_event);
    }
}
