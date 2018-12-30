package com.huji.foodtricks.buddies;

import java.io.Serializable;
import java.util.HashMap;


/**
 * Provides attendance tracking for event.
 */
@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class EventAttendanceProvider implements Serializable {

    private HashMap<String, String> invitees; // will maybe change if invitees become "People" objects.


    /// should there be an "invitees" or just these three separate groups?
    /// where should the person's attendance status be stored?
    private HashMap<String, String> attending;
    private HashMap<String, String> notAttending;
    private HashMap<String, String> tentatives;
    private HashMap<String, String> nonResponsive;

    public enum RSVP {ATTENDING, NOT_ATTENDING, TENTATIVE, NON_RESPONSIVE};

    public RSVP getUserRSVP(String userId) {
        if (attending.get(userId) != null)
            return RSVP.ATTENDING;
        if (tentatives.get(userId) != null)
            return RSVP.TENTATIVE;
        if (notAttending.get(userId) != null)
            return RSVP.NOT_ATTENDING;
        if (nonResponsive.get(userId) != null)
            return RSVP.NON_RESPONSIVE;
        return null;
    }

    public EventAttendanceProvider() {
        this.invitees = new HashMap<String, String>();
        this.nonResponsive = new HashMap<String, String>();
        this.attending = new HashMap<String, String>();
        this.notAttending = new HashMap<String, String>();
        this.tentatives = new HashMap<String, String>();
    }

    public EventAttendanceProvider(HashMap<String, String> invitees) {
        this.invitees = invitees; // reference ?
        this.nonResponsive = new HashMap<>(invitees);
        this.attending = new HashMap<String, String>();
        this.notAttending = new HashMap<String, String>();
        this.tentatives = new HashMap<String, String>();
    }

    public void markAttending(String userID) {
        if (!invitees.keySet().contains(userID)) { /// throw exception? assert?
            return;
        }
        tentatives.remove(userID);
        notAttending.remove(userID);
        attending.put(userID, invitees.get(userID));

        nonResponsive.remove(userID);
    }

    public void markNotAttending(String userID) {
        if (!invitees.keySet().contains(userID)) { /// throw exception? assert?
            return;
        }
        tentatives.remove(userID);
        notAttending.put(userID, invitees.get(userID));
        attending.remove(userID);
    }

    public void markTentative(String userID) {
        if (!invitees.keySet().contains(userID)) { /// throw exception? assert?
            return;
        }
        tentatives.put(userID, invitees.get(userID));
        notAttending.remove(userID);
        attending.remove(userID);
    }


    public HashMap<String, String> getAttending() {
        return attending;
    }

    public int getAttendingCount() {
        return attending.size();
    }

    public HashMap<String, String> getNotAttending() {
        return notAttending;
    }

    public int getNotAttendingCount() {
        return notAttending.size();
    }

    public HashMap<String, String> getTentatives() {
        return tentatives;
    }

    public int getTentativesCount() {
        return tentatives.size();
    }

    public HashMap<String, String> getNonResponsive() {
        return nonResponsive;
    }

    public int getNonResponsiveCount() {
        return nonResponsive.size();
    }

    public HashMap<String, String> getInvitees() {
        return invitees;
    }
}
