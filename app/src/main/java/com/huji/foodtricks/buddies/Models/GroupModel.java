package com.huji.foodtricks.buddies.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A User's group of other users.
 */
public class GroupModel implements Serializable {

    private String groupName;
    private HashMap<String, String> users;

    public GroupModel() {
        // Default constructor required for calls to DataSnapshot.getValue(GroupModel.class)
    }

    public GroupModel(String groupName, HashMap<String, String> users) {
        this.groupName = groupName;
        this.users = new HashMap<>(users);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public HashMap<String, String> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, String> users) {
        this.users = users;
    }

    public void addUserToGroup(String userId, String userName) {
        this.users.put(userId, userName);
    }

    public void removeUserFromGroup(String userId) {
        this.users.remove(userId);
    }
}
