package com.huji.foodtricks.buddies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huji.foodtricks.buddies.Models.UserModel;

import java.util.HashMap;

public class CreateGroupActivity extends AppCompatActivity {

    private UserModel currentUser;
    private String currentUserID;
    private HashMap<String, String> invitees;
    private String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Intent newEventIntent = getIntent();
        currentUser = (UserModel) newEventIntent
                .getSerializableExtra(getResources().getString(R.string.extra_current_user_model));
        currentUserID = newEventIntent
                .getStringExtra(getResources().getString(R.string.extra_current_user_id));
        invitees = new HashMap<>();
        invitees.put(currentUserID, currentUser.getUserName());

        setupGroupNameInput();
        setupUserList();
    }

    private void setupUserList() {
        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayoutForCheckBoxes);
        final HashMap<String, String> customGroupOfFriends = new HashMap<>();

        getUserNamesFromDB(new UsersMapFetchingCompletion() {
            @Override
            public void onFetchSuccess(HashMap<String, String> usersMap) {
                setupCheckboxes(linearLayout, usersMap);
            }

            @Override
            public void onNoUsersFound() {
                // show that there are no users?
            }
        });
    }

    private void getUserNamesFromDB(UsersMapFetchingCompletion completion) {
        DatabaseStreamer dbs = new DatabaseStreamer();
        dbs.fetchIdsOfAllUsersInDB(completion);
    }

    private void setupCheckboxes(LinearLayout containingLayout, HashMap<String, String> usersMap) {
        usersMap.remove(currentUserID);
        for (String userId : usersMap.keySet()) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(usersMap.get(userId));
            checkBox.setId(userId.hashCode());
            containingLayout.addView(checkBox);
        }
        setupCreateGroupButton(containingLayout, usersMap);
    }

    private void setupCreateGroupButton(LinearLayout containingLayout,
                                        HashMap<String, String> usersMap) {
        Button finalizeSelectionButton = new Button(containingLayout.getContext());
        finalizeSelectionButton.setBackgroundColor(getResources().getColor(R.color.colorEnabledFAB));
        finalizeSelectionButton.setText(R.string.create_group);
        finalizeSelectionButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        finalizeSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String userId : usersMap.keySet()) {
                    CheckBox checkBox = containingLayout.findViewById(userId.hashCode());
                    if (checkBox.isChecked()) {
                        invitees.put(userId , usersMap.get(userId));
                    }
                }
                onCreateGroupPressed();
            }
        });
        containingLayout.addView(finalizeSelectionButton);

    }

    private void onCreateGroupPressed() {
        hideSoftKeyboard();
        if (!tryToSaveGroupNameFromInput((TextView)findViewById(R.id.editText))) {
            return;
        }
        Intent finishCreatingGroupIntent = new Intent();
        finishCreatingGroupIntent.putExtra("Custom Group", invitees);
        finishCreatingGroupIntent.putExtra("Group Name", groupName);
        setResult(Activity.RESULT_OK, finishCreatingGroupIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    ////////////////
    // Group Name //
    ////////////////

    private void setupGroupNameInput() {
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    v.clearFocus();
                    return true;
                }
                return false;
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideSoftKeyboard();
                    tryToSaveGroupNameFromInput((TextView)view);
                }
            }
        });
    }

    private boolean tryToSaveGroupNameFromInput(TextView textView) {
        groupName = textView.getText().toString();
        if (currentUser.groupNameTaken(groupName)) {
            Toast groupNameTakenToast = Toast.makeText(getApplicationContext(),
                    "Group name already Taken!", Toast.LENGTH_LONG);
            groupNameTakenToast.show();
            return false;
        } else if (groupName.equals("")) {
            Toast groupNameEmptyToast = Toast.makeText(getApplicationContext(),
                    "Must choose group name!", Toast.LENGTH_LONG);
            groupNameEmptyToast.show();
            return false;
        }
        return true;
    }

    private void hideSoftKeyboard() {
        EditText editText = findViewById(R.id.editText);
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.clearFocus();
    }
}
