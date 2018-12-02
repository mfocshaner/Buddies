package com.huji.foodtricks.buddies;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        disableFAB();

        setupWhoHorizontalPicker();
        setupWhatTextInput();
        setupWhenPicker();
    }

    /// parameters to be passed to new event
    private Date _time;
    private String _eventTitle; // not well defined
    private List<String> _invitees; // will maybe change if invitees become "People" objects,

    public void chooseGroup(String groupName) {
        // set invitees to be the group
        ArrayList<String> group = new ArrayList<>();
        group.add("amit");
        group.add("michael");
        group.add("buddy");
        group.add("ido");
        _invitees = group;
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
        if ((_time != null) && (_eventTitle != null && !_eventTitle.equals(""))
                && (_invitees != null)) {
            enableFAB();
        }
    }

    public void chooseTime(Date time) {
        _time = time;
    }

    public void chooseTitle(String eventTitle) {
        _eventTitle = eventTitle;
    }


    /**
     * Called when "create event" button is clicked
     * should be enabled only if all parameters are chosen (disable button before that)
     *
     * @return EventModel with all chosen parameters
     */
    private EventModel createEvent() {
        EventModel newEvent = new EventModel(_eventTitle, _time, _eventTitle, _invitees);
        return newEvent;
    }
    
    private void setupWhoHorizontalPicker() {
        final HorizontalPicker hPicker = (HorizontalPicker) findViewById(R.id.whoHPicker);

        hPicker.setItems(EventParametersProvider.getWhoItems());

        HorizontalPicker.OnSelectionChangeListener listener = new HorizontalPicker.OnSelectionChangeListener() {
            @Override
            public void onItemSelect(HorizontalPicker picker, int index) {
                HorizontalPicker.PickerItem selected = picker.getSelectedItem();
                Toast.makeText(CreateEventActivity.this, selected.hasDrawable() ?
                        "Item at " + (picker.getSelectedIndex() + 1) + " is selected" :
                        selected.getText() + " is selected", Toast.LENGTH_SHORT).show();

                chooseGroup("the cool guys");
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
//                HorizontalPicker.PickerItem selected = picker.getSelectedItem();
//                Toast.makeText(CreateEventActivity.this, selected.hasDrawable() ?
//                        "Chosen time at " + (picker.getSelectedIndex() + 1) :
//                        selected.getText() + " is selected", Toast.LENGTH_SHORT).show();
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

    public void fabClicked(View view){
        EventModel newEvent = createEvent();
        // send this to server ?
        // show user?
        // return to previous activity (main screen) or launch details page?
        System.out.println("yayyyy");
    }



}
