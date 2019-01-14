package com.huji.foodtricks.buddies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.EventLog;

import com.google.firebase.auth.FirebaseAuth;
import com.huji.foodtricks.buddies.CommentsSection.CommentSectionFragment;
import com.huji.foodtricks.buddies.Models.EventModel;
import com.huji.foodtricks.buddies.Models.UserModel;

public class ViewCommentsActivity extends AppCompatActivity {

    private static EventModel curr_event;
    private String currentUserID;
    private final DatabaseStreamer dbs = new DatabaseStreamer();
    private String curr_event_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);

        Intent viewCommentsIntent = getIntent();
        curr_event = (EventModel) viewCommentsIntent
                .getSerializableExtra(getString(R.string.extra_current_event_model));
        curr_event_id = viewCommentsIntent.getStringExtra(getResources()
                .getString(R.string.extra_current_event_id));
        currentUserID = viewCommentsIntent
                .getStringExtra(getString(R.string.extra_current_user_id));

        CommentSectionFragment commentSectionFragment = (CommentSectionFragment)
                this.getSupportFragmentManager().findFragmentById(R.id.comment_section_fragment);
        if (commentSectionFragment != null) {
            Bundle bundleForCommentSection = new Bundle();

            DatabaseStreamer dbs = new DatabaseStreamer();
            dbs.fetchUserModelById(currentUserID, new UserFetchingCompletion() {
                @Override
                public void onFetchSuccess(UserModel userModel) {
                    dbs.fetchEventModelById(curr_event_id,
                            new EventFetchingCompletion() {
                        @Override
                        public void onFetchSuccess(EventModel eventModel) {
                            bundleForCommentSection.putString(getString(R.string.extra_current_event_id),
                                    curr_event_id);
                            bundleForCommentSection
                                    .putSerializable(getString(R.string.extra_current_event_model),
                                            eventModel);

                            bundleForCommentSection
                                    .putString(getString(R.string.extra_current_user_id), currentUserID);
                            bundleForCommentSection
                                    .putSerializable(getString(R.string.extra_current_user_model),
                                            userModel);

                            commentSectionFragment.setArguments(bundleForCommentSection);
                        }

                        @Override
                        public void onNoEventFound() {

                        }
                    });
                }

                @Override
                public void onNoUserFound() {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
