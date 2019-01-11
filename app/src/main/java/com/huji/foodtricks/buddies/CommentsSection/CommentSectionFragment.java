package com.huji.foodtricks.buddies.CommentsSection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.huji.foodtricks.buddies.FirebaseDB;
import com.huji.foodtricks.buddies.Models.CommentModel;
import com.huji.foodtricks.buddies.Models.EventModel;
import com.huji.foodtricks.buddies.Models.UserModel;
import com.huji.foodtricks.buddies.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CommentSectionFragment extends Fragment {
    ListView listView;
    private CommentListAdapter commentListAdapter;
    private ArrayList<CommentModel> commentModels = new ArrayList<>();
    private EventModel eventModel;
    private String eventId;
    private UserModel userModel;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comment_section, container, false);

        listView = rootView.findViewById(R.id.comments_list_view);
        TextView addCommentText = rootView.findViewById(R.id.addCommentText);
        addCommentText.setOnClickListener(this::addCommentClicked);

        return rootView;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        if (args != null) {
            eventModel = (EventModel) args.getSerializable(getString(R.string.extra_current_event_model));
            if (eventModel != null) {
                commentModels = eventModel.getComments();
            }
            eventId = args.getString(getString(R.string.extra_current_event_id));
            userId = args.getString(getString(R.string.extra_current_user_id));
            userModel = (UserModel) args.getSerializable(getString(R.string.extra_current_user_model));
        }
        this.commentListAdapter = new CommentListAdapter(getContext(), getCommentModels(),
                eventModel, eventId, userId);
        listView.setAdapter(this.commentListAdapter);
        setDbListener();
    }

    private ArrayList<CommentModel> getCommentModels() {
        if (commentModels == null) {
            commentModels = new ArrayList<>();
        }
        return commentModels;
    }

    private String inputText = "";

    public void addCommentClicked(View view) {
        Context context = this.getContext();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(getString(R.string.add_comment_dialog));


        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setPadding(3, 0, 3, 0);
        input.setSingleLine(false);
        input.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        dialogBuilder.setView(input);

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputText = input.getText().toString();
                addComment(inputText);
                dialog.dismiss();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialogBuilder.show();
    }

    private void addComment(String commentText) {
        Date rightNow = Calendar.getInstance().getTime();
        CommentModel newComment = new CommentModel(commentText, userId, userModel.getUserName(),
                userModel.getImageUrl(), rightNow);
        commentListAdapter.addComment(newComment);
    }

    private DatabaseReference DBref;

    private void setDbListener() {
        final FirebaseDatabase DB = FirebaseDB.getDatabase();

        DBref = DB.getReference("events/" + eventId + "/comments/");
        DBref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<CommentModel>> genericTypeIndicator =
                        new GenericTypeIndicator<ArrayList<CommentModel>>() {};
                ArrayList<CommentModel> updatedCommentModels =
                        dataSnapshot.getValue(genericTypeIndicator);
                if (updatedCommentModels == null) {
                    return;
                }
                commentModels = updatedCommentModels;
                commentListAdapter.notifyDataSetInvalidated();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
