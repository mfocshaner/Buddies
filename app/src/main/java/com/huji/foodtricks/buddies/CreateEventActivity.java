package com.huji.foodtricks.buddies;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
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
import java.util.Locale;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class CreateEventActivity extends AppCompatActivity {

    private UserModel currentUser;
    private String currentUserID;
    private final DatabaseStreamer dbs = new DatabaseStreamer();
    private TimePickerDialog tpd;

    /// parameters to be passed to new event
    private Date date;
    private GregorianCalendar calendar = new GregorianCalendar();
    private String eventTitle;
    private String chosenGroupName;
    private HashMap<String, String> invitees;

    private static final int CREATE_NEW_GROUP_REQUEST = 1;

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

        setupGroupSelection();
        setupWhatTextInput();
    }

    private void disableCreateEventButton(){
        Button createEventButton = findViewById(R.id.createEventButton);
        createEventButton.setEnabled(false);
        createEventButton.setBackgroundColor(getColor(R.color.colorDisabledCreateNewEventButton));
        createEventButton.setText(R.string.create_new_event_disabled);
        createEventButton.setTypeface(null, Typeface.NORMAL);
        createEventButton.setAlpha(0.4f);
    }

    private void enableCreateEventButton(){
        Button createEventButton = findViewById(R.id.createEventButton);
        createEventButton.setEnabled(true);
        createEventButton.setBackgroundColor(getColor(R.color.colorEnabledCreateNewEventButton));
        createEventButton.setAlpha(1f);
        createEventButton.setText(R.string.create_new_event_enabled);
        createEventButton.setTypeface(null, Typeface.BOLD);
        createEventButton.setTextColor(getColor(R.color.mdtp_white));
    }

    private void shouldEnableCreateEventButton(){
        if ((date != null) && (eventTitle != null && !eventTitle.equals(""))
                && (invitees != null)) {
            enableCreateEventButton();
        }
    }

    private void chooseTime(Date time) {
        this.date = time;
    }

    private void chooseTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    private EventModel createEventFromChoices() {
        return new EventModel(eventTitle, date, invitees, currentUserID, currentUser.getImageUrl());
    }

    ///////////////////////
    /// GROUP SELECTION ///
    ///////////////////////

    public void setupGroupSelection() {
        LinearLayout groupsLinearLayout = findViewById(R.id.who_linear_layout);
        for (GroupModel groupModel : currentUser.getGroups().values()) {
            setupGroupCard(groupsLinearLayout, groupModel);
        }
    }

    private View setupGroupCard(ViewGroup containingLayout, GroupModel groupModel) {
        View groupCardView = LayoutInflater.from(this).inflate(R.layout.group_card,
                containingLayout, false);
        groupCardView.setId(groupModel.getGroupName().hashCode());
        containingLayout.addView(groupCardView, 0);

        TextView groupNameTextView = groupCardView.findViewById(R.id.groupName);
        groupNameTextView.setText(groupModel.getGroupName());

        TextView groupMembersTextView = groupCardView.findViewById(R.id.groupMembers);
        groupMembersTextView.setText(getGroupMembersFirstNames(groupModel));

        ImageView eventImage = groupCardView.findViewById(R.id.groupCardImage);
        GlideApp.with(this).load(currentUser.getImageUrl()) // should be group image
                .override(170, 170)
                .apply(RequestOptions.circleCropTransform())
                .into(eventImage);

        groupCardView.setOnClickListener(view -> {
            hideSoftKeyboard();
            groupCardClicked(containingLayout, groupCardView, groupModel);
        });
        return groupCardView;
    }

    private String getGroupMembersFirstNames(GroupModel groupModel){
        HashMap<String, String> usersMap = groupModel.getUsers();
        usersMap.remove(currentUserID);
        ArrayList<String> userNames = new ArrayList<>(usersMap.values());

        ArrayList<String> firstNames = new ArrayList<>();
        for (String userName : userNames) {
            firstNames.add(userName.split(" ")[0]);
        }
        firstNames.add("You");
        return String.join(", ", firstNames);
    }

    private void groupCardClicked(ViewGroup containingLayout, View groupCardView,
                                  GroupModel groupModel) {
        unchooseChosenGroupCard(containingLayout);
        saveGroupChoice(groupModel);
        groupCardView.findViewById(R.id.group_card_linear_layout)
                .setBackgroundColor(getResources().getColor(R.color.selectedGroupCard));
        shouldEnableCreateEventButton();
    }

    private void unchooseChosenGroupCard(ViewGroup cardsContainer){
        if (chosenGroupName != null) {
            View groupCardView = cardsContainer.findViewById(chosenGroupName.hashCode());
            groupCardView.findViewById(R.id.group_card_linear_layout)
                    .setBackgroundColor(getResources().getColor(R.color.mdtp_white));
        }
    }

    private void saveGroupChoice(GroupModel groupModel) {
        chosenGroupName = groupModel.getGroupName();
        invitees = groupModel.getUsers();
    }



    ///////////////////
    // CUSTOM GROUPS //
    ///////////////////


    public void createCustomGroup(View view) {
        Intent createCustomGroup = new Intent(this, CreateGroupActivity.class);
        createCustomGroup.putExtra(getResources().getString(R.string.extra_current_user_model),
                currentUser);
        createCustomGroup.putExtra(getResources().getString(R.string.extra_current_user_id),
                currentUserID);
        startActivityForResult(createCustomGroup, CREATE_NEW_GROUP_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        // Check which request we're responding to
        if (requestCode == CREATE_NEW_GROUP_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                GroupModel newGroup = (GroupModel)resultIntent.getSerializableExtra(
                                getResources().getString(R.string.extra_custom_group));
                // create group for user in DB and on screen
                this.invitees = newGroup.getUsers();
                updateUserInDBWithNewGroup(newGroup);
            }
        }
    }

    private void updateUserInDBWithNewGroup(GroupModel groupToAdd) {
        currentUser.addGroup(groupToAdd);
        dbs.modifyUser(currentUser, currentUserID, ()
                -> updateGroupsListWithCustomGroup(groupToAdd));
    }

    private void updateGroupsListWithCustomGroup(GroupModel groupToAdd) {
        LinearLayout groupsLinearLayout = findViewById(R.id.who_linear_layout);
        View groupCardView = setupGroupCard(groupsLinearLayout, groupToAdd);
        groupCardClicked(groupsLinearLayout, groupCardView, groupToAdd);
    }

    ////////////
    /// WHAT ///
    ////////////

    private void setupWhatTextInput() {
        EditText editText = findViewById(R.id.whatTextInput);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                tryToGetTitleFromInput(editText);
                v.clearFocus();
            }
            return false;
        });
        editText.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                hideSoftKeyboard();
                tryToGetTitleFromInput(editText);
            }
        });
    }

    private void tryToGetTitleFromInput(EditText editTextView){
        String userText = editTextView.getText().toString();
        if (userText.replace(" ", "").equals("")){
            eventTitle = "";
            editTextView.setText("");
            disableCreateEventButton();
            Toast.makeText(this, "Event name can't be empty!", Toast.LENGTH_LONG).show();
            return;
        }
        chooseTitle(userText);
        shouldEnableCreateEventButton();
    }

    private void hideSoftKeyboard() {
        EditText editText = findViewById(R.id.whatTextInput);
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.clearFocus();
    }

    ////////////
    /// WHEN ///
    ////////////

    public void dateChosen(View view) {
        if (view.getId() != R.id.tomorrowButton && view.getId() != R.id.tonightButton) {
            return;
        }
        hideSoftKeyboard();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 20);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        if (view.getId() == R.id.tomorrowButton) {
            today.add(Calendar.DAY_OF_MONTH, 1);
        }
        chooseTime(today.getTime());
        shouldEnableCreateEventButton();
        chooseButtonAndUnchooseRest(view);
    }

    private void chooseButtonAndUnchooseRest(View view){
        unchooseAllDateButtons();
        view.setEnabled(false);
    }

    private void unchooseAllDateButtons(){
        Button tonightButton = findViewById(R.id.tonightButton);
        Button tomorrowButton = findViewById(R.id.tomorrowButton);
        Button customTimeButton = findViewById(R.id.customTimeButton);
        tonightButton.setEnabled(true);
        tomorrowButton.setEnabled(true);
        customTimeButton.setEnabled(true);
        customTimeButton.setText("⏰");
    }

    public void customDateClicked(View view){
        hideSoftKeyboard();
        unchooseAllDateButtons();
        showDateTimePicker();
        shouldEnableCreateEventButton();
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


    private void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        tpd.show(getSupportFragmentManager(), "Timepickerdialog");
    }


    private void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        chooseTime(calendar.getTime());
        changeTitleOfCustomDate(calendar);
    }


    private void changeTitleOfCustomDate(Calendar chosenDateTime) {
        String dateTime = MessageFormat.format("{4}\n{0}/{1} {2}:{3}",
                chosenDateTime.get(Calendar.DATE),
                chosenDateTime.get(Calendar.MONTH),
                chosenDateTime.get(Calendar.HOUR_OF_DAY),
                chosenDateTime.get(Calendar.MINUTE),
                chosenDateTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));

        Button customTimeButton = (Button)findViewById(R.id.customTimeButton);
        customTimeButton.setText(dateTime);
    }

    public void createNewEventButtonClicked(View view) {
        final EventModel createdEvent = createEventFromChoices();
        final String createdEventID = dbs.writeNewEventModel(createdEvent);

        updateInviteesWithNewEventID(createdEventID);
        moveToSingleEventView(createdEvent, createdEventID);
    }

    private void updateInviteesWithNewEventID(String eventID) {
        dbs.addEventIdToUserIdList(new ArrayList<>(invitees.keySet()), eventID,
                () -> {
                    // do nothing
                });
    }

    private void moveToSingleEventView(EventModel createdEvent, String createdEventID) {
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
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
        startActivity(viewEventIntent, options.toBundle());
        this.finish();
    }
}
