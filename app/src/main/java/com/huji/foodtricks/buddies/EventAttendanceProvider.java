package com.huji.foodtricks.buddies;

import java.io.Serializable;
import java.util.HashMap;


/**
 * Provides attendance tracking for event.
 */
@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class EventAttendanceProvider implements Serializable {

    private HashMap<String, String> invitees; // will maybe change if invitees become "People" objects.

    private HashMap<String, String> attending;
    private HashMap<String, String> notAttending;
    private HashMap<String, String> tentatives;
    private HashMap<String, String> nonResponsive;

    public enum RSVP {ATTENDING, NOT_ATTENDING, TENTATIVE, NON_RESPONSIVE}

    public RSVP getUserRSVP(String userId) {
        if (attending.containsKey(userId)) {
            return RSVP.ATTENDING;
        }
        if (tentatives.containsKey(userId)) {
            return RSVP.TENTATIVE;
        }
        if (notAttending.containsKey(userId)) {
            return RSVP.NOT_ATTENDING;
        }
        if (nonResponsive.containsKey(userId)) {
            return RSVP.NON_RESPONSIVE;
        }
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

        nonResponsive.remove(userID);
    }

    public void markTentative(String userID) {
        if (!invitees.keySet().contains(userID)) { /// throw exception? assert?
            return;
        }
        tentatives.put(userID, invitees.get(userID));
        notAttending.remove(userID);
        attending.remove(userID);

        nonResponsive.remove(userID);
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


    public void setInvitees(HashMap<String, String> invitees) {
        this.invitees = invitees;
    }

    public HashMap<String, String> getInvitees() {
        return invitees;
    }

    public void setAttending(HashMap<String, String> attending) {
        this.attending = attending;
    }

    public void setNotAttending(HashMap<String, String> notAttending) {
        this.notAttending = notAttending;
    }

    public void setTentatives(HashMap<String, String> tentatives) {
        this.tentatives = tentatives;
    }

    public void setNonResponsive(HashMap<String, String> nonResponsive) {
        this.nonResponsive = nonResponsive;
    }

}
