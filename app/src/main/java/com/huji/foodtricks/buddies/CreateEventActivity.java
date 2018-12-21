package com.huji.foodtricks.buddies;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.goodiebag.horizontalpicker.HorizontalPicker;
import com.huji.foodtricks.buddies.Models.EventModel;
import com.huji.foodtricks.buddies.Models.GroupModel;
import com.huji.foodtricks.buddies.Models.UserModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {

    private UserModel currentUserModel;
    private String currentUserId;
    private DatabaseStreamer dbs = new DatabaseStreamer();

    /// parameters to be passed to new event
    private Date time;
    private String eventTitle;
    private ArrayList<String> invitees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        disableFAB();

        Intent newEventIntent = getIntent();
        currentUserModel = (UserModel) newEventIntent
                .getSerializableExtra(EventsTabsActivity.EXTRA_CURRENT_USER);

        currentUserId = (String) newEventIntent.getStringExtra("userId");

        setupDummyGroups(); // not needed after we get actual user data

        setupWhoHorizontalPicker();
        setupWhatTextInput();
        setupWhenPicker();
    }

    private void setupDummyGroups() {
        ArrayList<String> firstGroup = new ArrayList<>();
        firstGroup.add("Amit the cool");
        firstGroup.add("Ido the sweet");
        firstGroup.add("Matan the busy");
        firstGroup.add("Michael the humble");
        currentUserModel.addGroup("cool guys", firstGroup);
        ArrayList<String> secondGroup = new ArrayList<>();
        secondGroup.add("Amit Silber");
        secondGroup.add("Ido Savion");
        secondGroup.add("Matan Harsat");
        secondGroup.add("Michael the Awesome");
        currentUserModel.addGroup("friendly guys", secondGroup);
//        dbs.writeNewUserModel(currentUserModel);
    }



    public void chooseGroup(String groupName) {
        GroupModel chosenGroup = currentUserModel.getGroupForName(groupName);
        invitees = new ArrayList<>(chosenGroup.getUserIds());
    }

    private void disableFAB(){
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setEnabled(false);
        fab.setAlpha(0.4f);
        fab.setImageAlpha(127);
    }

    private void enableFAB(){
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setEnabled(true);
        fab.setBackgroundTintList(this.getBaseContext().getResources()
                .getColorStateList(R.color.colorEnabledFAB));
        fab.setAlpha(1f);
        fab.setImageAlpha(255);
    }

    private void shouldEnableFAB(){
        if ((time != null) && (eventTitle != null && !eventTitle.equals(""))
                && (invitees != null)) {
            enableFAB();
        }
    }

    public void chooseTime(Date time) {
        this.time = time;
    }

    public void chooseTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    private EventModel createEventFromChoices() {
        EventModel newEvent = new EventModel(eventTitle, time, invitees,
                currentUserId);
        return newEvent;
    }
    
    private void setupWhoHorizontalPicker() {
        final HorizontalPicker hPicker = (HorizontalPicker) findViewById(R.id.whoHPicker);

        ArrayList<String> userGroupNames = new ArrayList<>(currentUserModel.getGroups().keySet());
        hPicker.setItems(EventParametersProvider.getWhoItems(userGroupNames));

        HorizontalPicker.OnSelectionChangeListener listener = new HorizontalPicker.OnSelectionChangeListener() {
            @Override
            public void onItemSelect(HorizontalPicker picker, int index) {
                HorizontalPicker.PickerItem selected = picker.getSelectedItem();
                String selectedGroupName = selected.getText();
                String toastMessage = selectedGroupName + " is selected";
                Toast.makeText(CreateEventActivity.this, selected.hasDrawable() ?
                        "Item at " + (picker.getSelectedIndex() + 1) + " is selected" :
                        toastMessage, Toast.LENGTH_SHORT).show();

                if (selectedGroupName.equals("Custom")) {
                    // custom group - create new group somehow!
                } else {
                    chooseGroup(selectedGroupName);
                }
                shouldEnableFAB();
            }
        };

        hPicker.setChangeListener(listener);
    }

    private void setupWhatTextInput() {
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String userText = v.getText().toString();
                    Toast.makeText(CreateEventActivity.this, userText, Toast.LENGTH_SHORT).show();
                    chooseTitle(userText);
                    shouldEnableFAB();
                    v.clearFocus();
                }
                return false;
            }
        });
    }

    private void setupWhenPicker() {
        HorizontalPicker hPicker = (HorizontalPicker) findViewById(R.id.whenHPicker);

        hPicker.setItems(EventParametersProvider.getWhenItems());

        HorizontalPicker.OnSelectionChangeListener listener =
                new HorizontalPicker.OnSelectionChangeListener() {
            @Override
            public void onItemSelect(HorizontalPicker picker, int index) {
                if (index < 0) {
                    return;
                }
                if (index == 0 || index == 1) { // tonight/tomorrow
                    Calendar today = Calendar.getInstance();
                    today.set(Calendar.HOUR_OF_DAY, 20);
                    today.set(Calendar.MINUTE, 0);
                    today.set(Calendar.SECOND, 0);

                    if (index == 1) { // tomorrow
                        today.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    chooseTime(today.getTime());
                } else {
                    showDateTimePicker();
                }
                shouldEnableFAB();
            }
        };

        hPicker.setChangeListener(listener);
    }

    private void showDateTimePicker() {
        final View dialogView = View.inflate(this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getHour(),
                        timePicker.getMinute());

                chooseTime(calendar.getTime());

                changeTitleOfCustomDate(calendar.getTime());

                alertDialog.dismiss();
            }});
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    private void changeTitleOfCustomDate(Date chosenDateTime) {
        HorizontalPicker whenHPicker = (HorizontalPicker) findViewById(R.id.whenHPicker);
        List<HorizontalPicker.PickerItem> oldItems = whenHPicker.getItems();
        oldItems.remove(2);

        oldItems.add(new HorizontalPicker.TextItem(chosenDateTime.toString()));

        HorizontalPicker.OnSelectionChangeListener listener = whenHPicker.getChangeListener();
        whenHPicker.setChangeListener(null); // disable so the onSelect function isn't called
        whenHPicker.setItems(oldItems, 2);
        whenHPicker.setChangeListener(listener);

    }

    public void createNewEventFabClicked(View view) {
        final EventModel createdEvent = createEventFromChoices();
        updateDatabaseWithNewEvent(createdEvent);
        moveToSingleEventView(view, createdEvent);
    }

    public void updateDatabaseWithNewEvent(final EventModel createdEvent) {
        String key = dbs.writeNewEventModel(createdEvent);
        dbs.addEventIdToUserIdList(createdEvent.getInviteesIDs(), key,
                new AddEventToUsersCompletion() {
            @Override
            public void onSuccess() {
                // send users a notification that they've been added to event?
                // who knows, right?
            }
        });
    }

    public void moveToSingleEventView(View view, EventModel createdEvent) {
        Intent viewEventIntent = new Intent(view.getContext(), ViewSingleEventActivity.class);
        viewEventIntent.putExtra("event", createdEvent);
        startActivity(viewEventIntent);
    }

    // note: after moving to new activity, if user clicks 'back', does he return to this activity
    // or to the EventsTabsActivity?
}
