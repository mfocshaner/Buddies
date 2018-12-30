package com.huji.foodtricks.buddies.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Model of a user, holding its details.
 */
public class UserModel implements Serializable {

    private String userName;
    private String imageUrl;
    private ArrayList<String> eventIDs;
    private HashMap<String, GroupModel> groups;
    private ArrayList<String> changedEvents = new ArrayList<>();
    // todo: i'm not entirely happy with having the name as key (to ensure there're no two groups
    // with the same name, and having the GroupModel hold the name as well, but it seems to be
    // the easier solution (to remove a group by name, add new group etc.)

    // todo: when we know how to add profile image, add profile image
    // todo: should user have anything showing which events are his? or we just get the events and
    // todo: ask each one whether the user is the organizer?

    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(UserModel.class)
    }

    public UserModel(String userName, String imageUrl) {
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.eventIDs = new ArrayList<>();
        this.groups = new HashMap<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<String> getEventIDs() {
        return eventIDs;
    }

    public void setEventIDs(ArrayList<String> eventIDs) {
        this.eventIDs = eventIDs;
    }

    public HashMap<String, GroupModel> getGroups() {
        return groups;
    }

    public void setGroups(HashMap<String, GroupModel> groups) {
        this.groups = groups;
    }

    public void addEventId(String eventId) {
        if (eventIDs == null) {
            eventIDs = new ArrayList<>();
        }
        if (!eventIDs.contains(eventId)) {
            eventIDs.add(eventId);
        }
    }

    public void removeEventId(String eventId) {
        eventIDs.remove(eventId);
    }

    public GroupModel getGroupForName(String groupName) {
        if (groups == null) {
            return null;
        }
        return groups.get(groupName);
    }

    public void removeGroup(String groupName) {
        groups.remove(groupName);
    }

    public void addGroup(String groupName, HashMap<String, String> groupMembersMap) {
        if (groups == null) {
            groups = new HashMap<>();
        }
        if (!groups.containsKey(groupName)) {
            GroupModel newGroup = new GroupModel(groupName, groupMembersMap);
            groups.put(groupName, newGroup);
        }
    }

    public boolean groupNameTaken(String groupName) {
        if (groups == null) {
            return false;
        }
        return groups.containsKey(groupName);
    }


    public ArrayList<String> getChangedEvents() {
        return changedEvents;
    }

    public void setChangedEvents(ArrayList<String> changedEvents) {
        this.changedEvents = changedEvents;
    }

    public void addChangedEvent(String changes) {
        if (this.changedEvents == null) {
            this.changedEvents = new ArrayList<>();
        }
        this.changedEvents.add(changes);
    }

    public void clearChangedEvents() {
        if (this.changedEvents == null) {
            this.changedEvents = new ArrayList<>();
            return;
        }
        this.changedEvents.clear();
    }
}
