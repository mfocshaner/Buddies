package com.huji.foodtricks.buddies;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goodiebag.horizontalpicker.HorizontalPicker;
import com.huji.foodtricks.buddies.Models.EventModel;
import com.huji.foodtricks.buddies.Models.GroupModel;
import com.huji.foodtricks.buddies.Models.UserModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class CreateEventActivity extends AppCompatActivity {

    private UserModel currentUser;
    private String currentUserID;
    private DatabaseStreamer dbs = new DatabaseStreamer();
    private TimePickerDialog tpd;

    /// parameters to be passed to new event
    private Date date;
    private GregorianCalendar calendar = new GregorianCalendar();
    private String eventTitle;
    private HashMap<String, String> invitees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        disableCreateEventButton();

        Intent newEventIntent = getIntent();
        currentUser = (UserModel) newEventIntent
                .getSerializableExtra(getResources().getString(R.string.extra_current_user_model));
        currentUserID = newEventIntent
                .getStringExtra(getResources().getString(R.string.extra_current_user_id));

        setupDummyGroups(); // not needed after we get actual user data

        setupWhoHorizontalPicker();
        setupWhatTextInput();
        setupWhenPicker();
    }

    private void setupDummyGroups() {
        HashMap<String, String> firstGroup = new HashMap<>();
        firstGroup.put("fakeId1", "Amit the cool");
        firstGroup.put("fakeId2", "Ido the sweet");
        firstGroup.put("fakeId3", "Matan the busy");
        firstGroup.put("fakeId4", "Michael the humble");
        currentUser.addGroup("cool guys", firstGroup);
        HashMap<String, String> secondGroup = new HashMap<>();
        secondGroup.put("fakeId1", "Amit Silber");
        secondGroup.put("fakeId2", "Ido Savion");
        secondGroup.put("fakeId3", "Matan Harsat");
        secondGroup.put("fakeId4", "Michael the Awesome");
        currentUser.addGroup("friendly guys", secondGroup);
    }



    public void chooseGroup(String groupName) {
        GroupModel chosenGroup = currentUser.getGroupForName(groupName);
        invitees = chosenGroup.getUsers();
    }

    private void disableCreateEventButton(){
        Button createEventButton = findViewById(R.id.createEventButton);
        createEventButton.setEnabled(false);
        createEventButton.setAlpha(0.4f);
    }

    private void enableCreateEventButton(){
        Button createEventButton = findViewById(R.id.createEventButton);
        createEventButton.setEnabled(true);
        createEventButton.setBackgroundTintList(this.getBaseContext().getResources()
                .getColorStateList(R.color.colorEnabledFAB));
        createEventButton.setAlpha(1f);
        createEventButton.setText(R.string.create_new_event_enabled);
    }

    private void shouldEnableCreateEventButton(){
        if ((date != null) && (eventTitle != null && !eventTitle.equals(""))
                && (invitees != null)) {
            enableCreateEventButton();
        }
    }

    public void chooseTime(Date time) {
        this.date = time;
    }

    public void chooseTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    private EventModel createEventFromChoices() {
        EventModel newEvent = new EventModel(eventTitle, date, invitees,
                currentUserID);
        return newEvent;
    }
    
    private void setupWhoHorizontalPicker() {
        final HorizontalPicker hPicker = (HorizontalPicker) findViewById(R.id.whoHPicker);

        ArrayList<String> userGroupNames = new ArrayList<>(currentUser.getGroups().keySet());
        hPicker.setItems(EventParametersProvider.getWhoItems(userGroupNames));

        HorizontalPicker.OnSelectionChangeListener listener = new HorizontalPicker.OnSelectionChangeListener() {
            @Override
            public void onItemSelect(HorizontalPicker picker, int index) {
                HorizontalPicker.PickerItem selected = picker.getSelectedItem();
                String selectedGroupName = selected.getText();

                final LinearLayout linearLayout =
                        (LinearLayout)findViewById(R.id.linearLayoutForCheckBoxes);
                linearLayout.removeAllViews();

                if (selectedGroupName.equals("Custom")) {
                    invitees = null;
                    disableCreateEventButton();
                    createGroupFromFriends();
                } else {
                    chooseGroup(selectedGroupName);
                }
                shouldEnableCreateEventButton();
            }
        };

        hPicker.setChangeListener(listener);
    }

    private void createGroupFromFriends() {
        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayoutForCheckBoxes);
        final HashMap<String, String> customGroupOfFriends = new HashMap<>();

        final HashMap<String, String> usersToChooseFrom = dummyUserNamesInDB();

        for (String userId : usersToChooseFrom.keySet()) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(usersToChooseFrom.get(userId));
            checkBox.setId(userId.hashCode());
            linearLayout.addView(checkBox);
        }

        Button finalizeSelectionButton = new Button(linearLayout.getContext());
        finalizeSelectionButton.setText(R.string.create_group);
        finalizeSelectionButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        finalizeSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String userId : usersToChooseFrom.keySet()) {
                    CheckBox checkBox = (CheckBox)linearLayout.findViewById(userId.hashCode());
                    if (checkBox.isChecked()) {
                        String userName = checkBox.getText().toString();
                        customGroupOfFriends.put(userId , usersToChooseFrom.get(userId));
                    }
                }

                linearLayout.removeAllViews();
                invitees = customGroupOfFriends;
                shouldEnableCreateEventButton();
            }
        });
        linearLayout.addView(finalizeSelectionButton);
    }

    private HashMap<String, String> dummyUserNamesInDB(){
        HashMap<String, String> users = new HashMap<>();
        users.put("BfsSucGUquSib4qKztVUz8SWDH42", "Michael Focshaner");
        users.put("5UUwOK8Ac6cvg4OSexrHBK7Wi952", "Amit Silber");
        users.put("LPUwbrBuQod8Sbaj1nT5uzOJe812", "Ido Savion");
        users.put("YLsU95DSh3dEFDmW7z8SCx5el382", "Matan Harsat");
        return users;
    }

    private void setupWhatTextInput() {
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                final LinearLayout linearLayout =
                        (LinearLayout)findViewById(R.id.linearLayoutForCheckBoxes);
                linearLayout.removeAllViews();

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String userText = v.getText().toString();
                    chooseTitle(userText);
                    shouldEnableCreateEventButton();
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
                final LinearLayout linearLayout =
                        (LinearLayout)findViewById(R.id.linearLayoutForCheckBoxes);
                linearLayout.removeAllViews();

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
                shouldEnableCreateEventButton();
            }
        };

        hPicker.setChangeListener(listener);
    }

    private void showDateTimePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this::onDateSet,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_1);
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
        tpd = TimePickerDialog.newInstance(
                this::onTimeSet,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setTimeInterval(1, 15, 60);
    }


    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        tpd.show(getSupportFragmentManager(), "Timepickerdialog");
    }


    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        chooseTime(calendar.getTime());
        changeTitleOfCustomDate(calendar);
    }


    private void changeTitleOfCustomDate(Calendar chosenDateTime) {
        HorizontalPicker whenHPicker = (HorizontalPicker) findViewById(R.id.whenHPicker);
        List<HorizontalPicker.PickerItem> oldItems = whenHPicker.getItems();
        oldItems.remove(2);

        String dateTime = MessageFormat.format("{4}\n{0}/{1} {2}:{3}",
                chosenDateTime.get(Calendar.DATE),
                chosenDateTime.get(Calendar.MONTH),
                chosenDateTime.get(Calendar.HOUR_OF_DAY),
                chosenDateTime.get(Calendar.MINUTE),
                chosenDateTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
        oldItems.add(new HorizontalPicker.TextItem(dateTime));

        HorizontalPicker.OnSelectionChangeListener listener = whenHPicker.getChangeListener();
        whenHPicker.setChangeListener(null); // disable so the onSelect function isn't called
        whenHPicker.setItems(oldItems, 2);
        whenHPicker.setChangeListener(listener);

    }

    public void createNewEventButtonClicked(View view) {
        final EventModel createdEvent = createEventFromChoices();
        final String createdEventID = dbs.writeNewEventModel(createdEvent);

        updateInviteesWithNewEventID(createdEventID);
        moveToSingleEventView(createdEvent, createdEventID);
    }

    public void updateInviteesWithNewEventID(String eventID) {
        dbs.addEventIdToUserIdList(new ArrayList<String>(invitees.keySet()), eventID,
                new AddEventToUsersCompletion() {
            @Override
            public void onSuccess() {
                // do nothing
            }
        });
    }

    public void moveToSingleEventView(EventModel createdEvent, String createdEventID) {
        Intent viewEventIntent = new Intent(this, ViewSingleEventActivity.class);

        viewEventIntent.putExtra(getResources().getString(R.string.extra_current_user_model),
                currentUser);
        viewEventIntent.putExtra(getResources().getString(R.string.extra_current_user_id),
                currentUserID);
        viewEventIntent.putExtra(getResources().getString(R.string.extra_current_event_model),
                createdEvent);
        viewEventIntent.putExtra(getResources().getString(R.string.extra_current_event_id),
                createdEventID);

        viewEventIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(viewEventIntent);
        this.finish();
    }

}
