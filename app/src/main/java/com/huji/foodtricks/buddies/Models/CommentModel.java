package com.huji.foodtricks.buddies.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class CommentModel implements Serializable {

    private String message;
    private String writerUID;
    private String writerUserName;
    private String writerImageURL;
    private Date writingTime;
    private ArrayList<String> upvoters;
    private ArrayList<String> downvoters;

    public CommentModel() {
    }

    public CommentModel(String message, String writerUID, String writerUserName, String writerImageURL, Date writingTime) {
        this.message = message;
        this.writerUID = writerUID;
        this.writerUserName = writerUserName;
        this.writingTime = writingTime;
        this.writerImageURL = writerImageURL;
        this.upvoters = new ArrayList<>();
        this.downvoters = new ArrayList<>();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWriterUID() {
        return writerUID;
    }

    public void setWriterUID(String writerUID) {
        this.writerUID = writerUID;
    }

    public String getWriterUserName() {
        return writerUserName;
    }

    public void setWriterUserName(String writerUserName) {
        this.writerUserName = writerUserName;
    }

    public String getWriterImageURL() {
        return writerImageURL;
    }

    public void setWriterImageURL(String writerImageURL) {
        this.writerImageURL = writerImageURL;
    }

    public Date getWritingTime() {
        return writingTime;
    }

    public void setWritingTime(Date writingTime) {
        this.writingTime = writingTime;
    }

    public ArrayList<String> getUpvoters() {
        if (upvoters == null) {
            upvoters = new ArrayList<>();
        }
        return upvoters;
    }

    public void setUpvoters(ArrayList<String> upvoters) {
        this.upvoters = upvoters;
    }

    public ArrayList<String> getDownvoters() {
        if (downvoters == null) {
            downvoters = new ArrayList<>();
        }
        return downvoters;
    }

    public void setDownvoters(ArrayList<String> downvoters) {
        this.downvoters = downvoters;
    }

    public int upvotes() {
        if (upvoters != null) {
            return upvoters.size();
        }
        return 0;
    }


    public int downvotes() {
        if (downvoters != null) {
            return downvoters.size();
        }
        return 0;
    }

    public void voteUp(String votingUserId) {
//        if (votingUserId.equals(writerUID)) {  // it's reasonable that a user can't vote on his
//            return;                            // own comment, but it's commented out to allow
//        }                                      // demonstration.
        if (downvoters != null) {
            downvoters.remove(votingUserId);
        }
        if (upvoters == null) {
            upvoters = new ArrayList<>();
            upvoters.add(votingUserId);
        } else if (!upvoters.contains(votingUserId)) {
            upvoters.add(votingUserId);
        }
    }

    public void voteDown(String votingUserId) {
//        if (votingUserId.equals(writerUID)) {  // it's reasonable that a user can't vote on his
//            return;                            // own comment, but it's commented out to allow
//        }                                      // demonstration.
        if (upvoters != null) {
            upvoters.remove(votingUserId);
        }
        if (downvoters == null) {
            downvoters = new ArrayList<>();
            downvoters.add(votingUserId);
        } else if (!downvoters.contains(votingUserId)) {
            downvoters.add(votingUserId);
        }
    }
}
