package com.huji.foodtricks.buddies.CommentsSection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huji.foodtricks.buddies.DatabaseStreamer;
import com.huji.foodtricks.buddies.GlideApp;
import com.huji.foodtricks.buddies.Models.CommentModel;
import com.huji.foodtricks.buddies.Models.EventModel;
import com.huji.foodtricks.buddies.R;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import androidx.annotation.NonNull;

public class CommentListAdapter extends ArrayAdapter<CommentModel> {

    private final Context context;
    private ArrayList<CommentModel> commentModels;
    private final EventModel eventModel;
    private final String eventId;
    private final String userId;

    public CommentListAdapter(@NonNull Context context, ArrayList<CommentModel> commentModels,
                              EventModel eventModel, String eventId, String userId) {
        super(context, 0, commentModels);
        this.context = context;
        this.commentModels = commentModels;
        this.eventModel = eventModel;
        this.eventId = eventId;
        this.userId = userId;
    }

    @Override
    public View getView(int position, View commentView, ViewGroup parent) {
        if (position >= commentModels.size()) {
            return null;
        }
        CommentModel commentModel = commentModels.get(position);

        if (commentView == null) {
            commentView = LayoutInflater.from(getContext())
                    .inflate(R.layout.comment_card, parent, false);
        }

        commentView.setId(position);
        setupCommentCardBackgroundColor(position, commentView);

        TextView writerUserNameTextView = (TextView) commentView.findViewById(R.id.writerUserName);
        TextView commentDateTextView = (TextView) commentView.findViewById(R.id.commentDate);
        TextView commentMessageTextView = (TextView) commentView.findViewById(R.id.commentMessage);
        ImageView writerAvatarImageView = (ImageView) commentView.findViewById(R.id.writerImage);

        writerUserNameTextView.setText(commentModel.getWriterUserName());
        commentMessageTextView.setText(commentModel.getMessage());

        commentDateTextView.setText(getFormattedDateString(commentModel.getWritingTime()));

        commentView.setOnClickListener(this::commentClick);

        String writerAvatarURL = commentModel.getWriterImageURL();
        if (!writerAvatarURL.equals("")) {
            GlideApp.with(context).load(writerAvatarURL)
                    .override(170)
                    .apply(RequestOptions.circleCropTransform())
                    .into(writerAvatarImageView);
        }

        updateVotesTextView(commentView, commentModel);

        return commentView;
    }

    private void setupCommentCardBackgroundColor(int position, View commentView) {
        if (position % 2 == 0) {
            commentView.findViewById(R.id.comment_card_linear_layout)
                    .setBackgroundColor(getContext().getColor(R.color.commentDarkBackground));
        } else {
            commentView.findViewById(R.id.comment_card_linear_layout)
                    .setBackgroundColor(getContext().getColor(R.color.commentLightBackground));
        }
    }

    private String getFormattedDateString(Date date) {
        Calendar dateTime = new GregorianCalendar();
        dateTime.setTime(date);
        String dateTimeString = MessageFormat.format("{0}/{1} {2}:{3}",
                dateTime.get(Calendar.DATE),
                dateTime.get(Calendar.MONTH) + 1,
                dateTime.get(Calendar.HOUR_OF_DAY),
                String.format(Locale.getDefault(),"%02d",dateTime.get(Calendar.MINUTE)));
        return dateTimeString;
    }

    private void updateVotesTextView(View commentView, CommentModel commentModel) {
        TextView votesTextView = (TextView) commentView.findViewById(R.id.votes_text_view);
        String newVotesString = String.format(context.getString(R.string.votes_text_formatted),
                commentModel.upvotes(),
                commentModel.downvotes());
        votesTextView.setText(newVotesString);
    }

    private boolean commentClick(View view) {
        Context context = this.getContext();
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        dialogBuilder.setTitle(context.getString(R.string.vote_comment_dialog));

        final int position = view.getId();
        dialogBuilder.setNeutralButton("UPVOTE", (dialog, which) -> {
            voteOnComment(position, true, view);
            dialog.dismiss();
        });
        dialogBuilder.setPositiveButton("DOWNVOTE", (dialog, which) -> {
            voteOnComment(position, false, view);
            dialog.dismiss();
        });

        dialogBuilder.show();
        return true;
    }

    private void voteOnComment(int position, boolean upvote, View commentView) {
        CommentModel votedComment = commentModels.get(position);
        if (upvote) {
            votedComment.voteUp(userId);
        } else {
            votedComment.voteDown(userId);
        }
        updateVotesTextView(commentView, votedComment);

        eventModel.setComments(commentModels);
        DatabaseStreamer dbs = new DatabaseStreamer();
        dbs.updateCommentWithVote(eventId, userId, upvote, position);
    }

    public void addComment(CommentModel commentModel) {
        commentModels.add(commentModel);
        eventModel.setComments(commentModels);
        DatabaseStreamer dbs = new DatabaseStreamer();
        dbs.addCommentToEvent(eventId, commentModel);
        this.notifyDataSetChanged();
    }
}
