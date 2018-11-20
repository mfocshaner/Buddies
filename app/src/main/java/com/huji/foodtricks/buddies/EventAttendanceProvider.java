package com.huji.foodtricks.buddies;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides attendance tracking for event.
 */
public class EventAttendanceProvider {

    private List<String> invitees; // will maybe change if invitees become "People" objects.

    /// should there be an "invitees" or just these three separate groups?
    /// where should the person's attendance status be stored?
    private ArrayList<String> attending;
    private ArrayList<String> notAttending;
    private ArrayList<String> tentatives;

    public EventAttendanceProvider(List<String> invitees) {
        this.invitees = invitees;
    }

    public void markAttending(String userName) {
        if (!invitees.contains(userName)) { /// throw exception? assert?
            return;
        }
        tentatives.remove(userName);
        notAttending.remove(userName);
        attending.add(userName);
    }

    public void markNotAttending(String userName) {
        if (!invitees.contains(userName)) { /// throw exception? assert?
            return;
        }
        tentatives.remove(userName);
        notAttending.add(userName);
        attending.remove(userName);
    }

    public void markTentative(String userName) {
        if (!invitees.contains(userName)) { /// throw exception? assert?
            return;
        }
        tentatives.add(userName);
        notAttending.remove(userName);
        attending.remove(userName);
    }


    public List<String> getAttending() {
        return attending;
    }

    public int getAttendingCount() {
        return attending.size();
    }

    public List<String> getNotAttending() {
        return notAttending;
    }

    public int getnoTAttendingCount() {
        return notAttending.size();
    }

    public List<String> getTentatives() {
        return tentatives;
    }

    public int getTentativesCount() {
        return tentatives.size();
    }

}
