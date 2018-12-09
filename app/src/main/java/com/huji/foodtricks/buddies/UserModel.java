package com.huji.foodtricks.buddies;

import java.util.ArrayList;
import java.util.List;

/**
 * Model of a user, holding its details.
 */
public class UserModel {

    private String userFirebaseId; /// IMPORTANT NOTE: not the ID of the model, but the
    // the id of the registers user. Any better name would be welcome!

    private String userName;
    private String firstName;
    private String lastName;
    private List<String> EventIDs;
    // todo: when we know how to add profile image, add profile image
    // todo: should user have anything showing which events are his? or we just get the events and
    // todo: ask each one whether the user is the organizer?

    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(UserModel.class)
    }

    public UserModel(String userFirebaseId, String userName, String firstName, String lastName) {
        this.userFirebaseId = userFirebaseId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.EventIDs = new ArrayList<>(1);
    }

    public String getUserFirebaseId() {
        return userFirebaseId;
    }

    public void setUserFirebaseId(String userFirebaseId) {
        this.userFirebaseId = userFirebaseId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getEventIDs() {
        return EventIDs;
    }

    public void setEventIDs(List<String> eventIDs) {
        EventIDs = eventIDs;
    }

    public void addEventId(String eventId) {
        if (EventIDs == null) {
            EventIDs = new ArrayList<>(1);
        }
        EventIDs.add(eventId);
    }

    public void removeEventId(String eventId) {
        EventIDs.remove(eventId);
    }
}
