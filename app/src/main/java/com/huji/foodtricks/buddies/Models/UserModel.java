package com.huji.foodtricks.buddies.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Model of a user, holding its details.
 */
public class UserModel implements Serializable {

    private String userAuthenticationId; /// IMPORTANT NOTE: not the ID of the model, but the
    // the id of the registered user as shown in Firebase->Authentication->Users.

    private String userName;
    private String firstName;
    private String lastName;
    private Set<String> EventIDs;

    private Map<String, GroupModel> groups;
    // todo: i'm not entirely happy with having the name as key (to ensure there're no two groups
    // with the same name, and having the GroupModel hold the name as well, but it seems to be
    // the easier solution (to remove a group by name, add new group etc.)

    // todo: when we know how to add profile image, add profile image
    // todo: should user have anything showing which events are his? or we just get the events and
    // todo: ask each one whether the user is the organizer?

    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(UserModel.class)
    }

    public UserModel(String userAuthenticationId, String userName, String firstName, String lastName) {
        this.userAuthenticationId = userAuthenticationId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.EventIDs = new HashSet<>();
        this.groups = new HashMap<>();
    }

    public String getUserAuthenticationId() {
        return userAuthenticationId;
    }

    public void setUserAuthenticationId(String userAuthenticationId) {
        this.userAuthenticationId = userAuthenticationId;
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
        return new ArrayList<>(EventIDs);
    }

    public void setEventIDs(List<String> eventIDs) {
        EventIDs = new HashSet<>(eventIDs);
    }

    public void addEventId(String eventId) {
        if (EventIDs == null) {
            EventIDs = new HashSet<>();
        }
        EventIDs.add(eventId);
    }

    public void removeEventId(String eventId) {
        EventIDs.remove(eventId);
    }

    public Map<String, GroupModel> groupMap() {
        return groups;
    }

    public List<GroupModel> getGroups() {
        return new ArrayList<>(groups.values());
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

    public void addGroup(String groupName, List<String> groupMembersIds) {
        if (groups == null) {
            groups = new HashMap<>();
        }
        if (!groups.containsKey(groupName)) {
            GroupModel newGroup = new GroupModel(groupName, groupMembersIds);
            groups.put(groupName, newGroup);
        }
    }

    public boolean groupNameTaken(String groupName) {
        if (groups == null) {
            return false;
        }
        return groups.containsKey(groupName);
    }

}
