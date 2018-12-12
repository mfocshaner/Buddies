package com.huji.foodtricks.buddies.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * A User's group of other users.
 */
public class GroupModel implements Serializable {

    private String groupName;
    private HashSet<String> userIds;

    public GroupModel(String groupName, List<String> userIds) {
        this.groupName = groupName;
        this.userIds = new HashSet<>(userIds);
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

    public void setUserIds(HashSet<String> userIds) {
        this.userIds = userIds;
    }

    public void addUserToGroup(String userId) {
        this.userIds.add(userId);
    }

    public void removeUserFromGroup(String userId) {
        this.userIds.remove(userId);
    }
}
