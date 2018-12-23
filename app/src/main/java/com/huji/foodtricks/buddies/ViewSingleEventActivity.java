package com.huji.foodtricks.buddies;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huji.foodtricks.buddies.Models.EventModel;
import com.huji.foodtricks.buddies.Models.UserModel;

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


public class ViewSingleEventActivity extends AppCompatActivity {

    static EventModel curr_event;
    private UserModel currentUser;
    private String currentUserID;
    private DatabaseStreamer dbs = new DatabaseStreamer();
    private String curr_event_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent eventCard = getIntent();
        curr_event = (EventModel) eventCard.getSerializableExtra(getResources().getString(R.string.extra_current_event_model));
        curr_event_id = eventCard.getStringExtra(getResources().getString(R.string.extra_current_event_id));
        currentUser = (UserModel) eventCard.getSerializableExtra(getResources().getString(R.string.extra_current_user_model));
        if (curr_event_id == null) {
            curr_event_id = "ABCDEFG";
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_event);

        Objects.requireNonNull(getSupportActionBar()).setTitle(curr_event.getTitle());
        updateAllFields(curr_event);
    }

    private void updateAllFields(EventModel curr_event) {

        TextView what_tv = findViewById(R.id.what_text);
        what_tv.setText(curr_event.getTitle());

        TextView date_tv = findViewById(R.id.date_textView);
        date_tv.setText(curr_event.getTime().toString());
        modifyDateTextView(curr_event.getTime(), date_tv);

        TextView hour_tv = findViewById(R.id.hour_textView);
        modifyHourTextView(curr_event.getTime(), hour_tv);


        TextView rsvpText = findViewById(R.id.RSVPText);
        modifyAttendersTextView(curr_event.getAttendanceProvider(), rsvpText);
    }

    private void modifyDateTextView(Date time, TextView date_tv) {
        // TODO : might want to use this format, we need to decide
        // DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        DateFormat formatter = new SimpleDateFormat("dd/MM", Locale.getDefault());
        try {
            time = formatter.parse(formatter.format(time));
        } catch (ParseException e) {
            date_tv.setText("Invalid date");
        }
        date_tv.setText(formatter.format(time));

    }

    private void modifyHourTextView(Date time, TextView hour_tv) {
        Formatter fmt = new Formatter();
        fmt.format("%tl:%tM", time, time);
        hour_tv.setText(fmt.toString());
    }


    public void modifyAttendersTextView(EventAttendanceProvider eventAttendanceProvider, TextView tv) {
        SpannableString attending = new SpannableString(
                String.join("\n", curr_event.getAttendanceProvider().getAttending()));
        SpannableString tentative = new SpannableString(
                String.join("\n", curr_event.getAttendanceProvider().getTentatives()));
        SpannableString not_attending = new SpannableString(
                String.join("\n", curr_event.getAttendanceProvider().getNotAttending()));
        SpannableString not_responsive = new SpannableString(
                String.join("\n", curr_event.getAttendanceProvider().getNonResponsive()));

        // setting the string's style:
        int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        attending.setSpan(new ForegroundColorSpan(Color.GREEN), 0, attending.length(), flag);
        tentative.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, tentative.length(), flag);
        not_attending.setSpan(new ForegroundColorSpan(Color.RED), 0, not_attending.length(), flag);
        not_responsive.setSpan(new ForegroundColorSpan(Color.GRAY), 0, not_responsive.length(), flag);
        SpannableStringBuilder builder = new SpannableStringBuilder(); // to concatenate string together
        builder.append(attending);
        builder.append(tentative);
        builder.append(not_attending);
        builder.append(not_responsive);
        tv.setText(builder);
    }

    public void onRSVPChangeClick(View view) {
//        Toast.makeText(ViewSingleEventActivity.this, getString(R.string.change_rsvp_msg_prefix) + .getTitle(), Toast.LENGTH_SHORT).show();
        EventAttendanceProvider attendanceProvider = curr_event.getAttendanceProvider();
        if (currentUser == null)
            return;
        if (view.getId() == R.id.approve_btn) {
            attendanceProvider.markAttending(currentUser.getUserName());

        } else if (view.getId() == R.id.tentative_btn) {
            attendanceProvider.markTentative(currentUser.getUserName());
        } else if (view.getId() == R.id.decline_btn) {
            attendanceProvider.markNotAttending(currentUser.getUserName());
        }
        curr_event.setAttendanceProvider(attendanceProvider);
        dbs.modifyEvent(curr_event, curr_event_id, new EventUpdateCompletion() {
            @Override
            public void onUpdateSuccess() {
                Toast update_event_updated = Toast.makeText(getApplicationContext(),
                        "Update completed", Toast.LENGTH_SHORT);
                update_event_updated.show();

            }

        });
        TextView rsvp_tv = findViewById(R.id.RSVPText);
        modifyAttendersTextView(curr_event.getAttendanceProvider(), rsvp_tv);
        //TODO: refactor the following code
        Set<Integer> allRSVPButtons = new HashSet<>(Arrays.asList(R.id.approve_btn, R.id.tentative_btn, R.id.decline_btn));
        allRSVPButtons.remove(view.getId()); // remove the selected button from the list of buttons to disable
        Button selectedButtonView = (Button) view; // just for readability
        selectedButtonView.setBackgroundColor(getResources().getColor(R.color.selectedRSVPButton));
        selectedButtonView.setTextColor(Color.WHITE);
//        setContentView(R.layout.activity_view_single_event);

        for (int buttonId : allRSVPButtons) {
            Button currButton = findViewById(buttonId);
            currButton.setBackgroundColor(Color.WHITE);
            currButton.setTextColor(Color.BLACK);
        }
    }
}
