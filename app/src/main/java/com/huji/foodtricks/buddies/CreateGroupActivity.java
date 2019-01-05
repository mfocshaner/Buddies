package com.huji.foodtricks.buddies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.goodiebag.horizontalpicker.HorizontalPicker;
import com.google.android.material.resources.TextAppearance;
import com.huji.foodtricks.buddies.Models.UserModel;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Objects;

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
        final LinearLayout linearLayout = findViewById(R.id.linearLayoutForGroupCheckBoxes);

        getUsersFromDB(new UsersMapFetchingCompletion() {
            @Override
            public void onFetchSuccess(HashMap<String, UserModel> usersMap) {
                setupCheckboxes(linearLayout, usersMap);
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

    private void setupCheckboxes(LinearLayout containingLayout, HashMap<String, UserModel> usersMap) {
        usersMap.remove(currentUserID);
        for (String userId : usersMap.keySet()) {
            final LinearLayout rowLinearLayout = new LinearLayout(this);
            rowLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLinearLayout.setVerticalGravity(Gravity.CENTER);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(Objects.requireNonNull(usersMap.get(userId)).getUserName());
            checkBox.setTextSize(20);
            checkBox.setId(userId.hashCode());
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ImageView checkboxImage = new ImageView(this);
            GlideApp.with(this)
                    .load(Objects.requireNonNull(usersMap.get(userId)).getImageUrl())
                    .override(200, 200)
                    .apply(RequestOptions.circleCropTransform())
                    .into(checkboxImage);
            rowLinearLayout.addView(checkboxImage);
            rowLinearLayout.addView(checkBox);
            rowLinearLayout.setPadding(0, 10, 0, 10);
            containingLayout.addView(rowLinearLayout);
        }
        setupCreateGroupButton(containingLayout, usersMap);
    }

    private void setupCreateGroupButton(LinearLayout containingLayout,
                                        HashMap<String, UserModel> usersMap) {
        Button finalizeSelectionButton = new Button(containingLayout.getContext());
        finalizeSelectionButton.setBackgroundColor(getResources().getColor(R.color.colorEnabledFAB, getTheme()));
        finalizeSelectionButton.setText(R.string.create_group);
        finalizeSelectionButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        finalizeSelectionButton.setOnClickListener(v -> {
            for (String userId : usersMap.keySet()) {
                CheckBox checkBox = containingLayout.findViewById(userId.hashCode());
                if (checkBox.isChecked()) {
                    invitees.put(userId , Objects.requireNonNull(usersMap.get(userId)).getUserName());
                }
            }
            onCreateGroupPressed();
        });
        containingLayout.addView(finalizeSelectionButton);

    }

    private void onCreateGroupPressed() {
        hideSoftKeyboard();
        if (!tryToSaveGroupNameFromInput(findViewById(R.id.editText))) {
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
        EditText editText = findViewById(R.id.editText);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus();
                return true;
            }
            return false;
        });
        editText.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                hideSoftKeyboard();
                tryToSaveGroupNameFromInput((TextView)view);
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
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.clearFocus();
    }
}
