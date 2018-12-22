package com.huji.foodtricks.buddies;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.HashMap;
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
        disableCreateEventButton();

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
    }



    public void chooseGroup(String groupName) {
        GroupModel chosenGroup = currentUserModel.getGroupForName(groupName);
        invitees = new ArrayList<>(chosenGroup.getUserIds());
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
        if ((time != null) && (eventTitle != null && !eventTitle.equals(""))
                && (invitees != null)) {
            enableCreateEventButton();
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
        final ArrayList<String> groupOfFriends = new ArrayList<>();

        final HashMap<String, String> usersToChooseFrom = dummyUserNamesInDB();
        int id = 0;
        for (String userName : usersToChooseFrom.keySet()) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(userName);
            checkBox.setId(id++);
            linearLayout.addView(checkBox);
        }

        Button finalizeSelectionButton = new Button(linearLayout.getContext());
        finalizeSelectionButton.setText(R.string.create_group);
        finalizeSelectionButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        finalizeSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < usersToChooseFrom.keySet().size(); i++) {
                    CheckBox checkBox = (CheckBox)linearLayout.findViewById(i);
                    if (checkBox.isChecked()) {
                        String userId = checkBox.getText().toString();
                        groupOfFriends.add(usersToChooseFrom.get(userId));
                    }
                }
                linearLayout.removeAllViews();
                invitees = groupOfFriends;
            }
        });
        linearLayout.addView(finalizeSelectionButton);
    }

    private HashMap<String, String> dummyUserNamesInDB(){
        HashMap<String, String> users = new HashMap<>();
        users.put("Michael", "BfsSucGUquSib4qKztVUz8SWDH42");
        users.put("Amit", "5UUwOK8Ac6cvg4OSexrHBK7Wi952");
        users.put("Ido","LPUwbrBuQod8Sbaj1nT5uzOJe812");
        users.put("Matan","YLsU95DSh3dEFDmW7z8SCx5el382");
        return users;
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

    public void createNewEventButtonClicked(View view) {
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
                // do nothing
            }
        });
    }

    public void moveToSingleEventView(View view, EventModel createdEvent) {
        Intent viewEventIntent = new Intent(view.getContext(), ViewSingleEventActivity.class);
        viewEventIntent.putExtra("event", createdEvent);
        viewEventIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(viewEventIntent);
        this.finish();
    }

}
