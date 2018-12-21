package com.huji.foodtricks.buddies.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A User's group of other users.
 */
public class GroupModel implements Serializable {

    private String groupName;
    private ArrayList<String> userIds;

    public GroupModel() {
        // Default constructor required for calls to DataSnapshot.getValue(GroupModel.class)
    }

    public GroupModel(String groupName, List<String> userIds) {
        this.groupName = groupName;
        this.userIds = new ArrayList<>(userIds);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getUserIds() {
        return new ArrayList<>(userIds);
    }

    public void setUserIds(ArrayList<String> userIds) {
        this.userIds = userIds;
    }

    public void addUserToGroup(String userId) {
        this.userIds.add(userId);
    }

    public void removeUserFromGroup(String userId) {
        this.userIds.remove(userId);
    }
}
