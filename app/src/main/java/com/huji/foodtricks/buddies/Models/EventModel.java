package com.huji.foodtricks.buddies.Models;

import com.huji.foodtricks.buddies.EventAttendanceProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model of an event, holding its details.
 */
public class EventModel implements Serializable {

    enum state {PAST, UPCOMING, PENDING, DISCARDED;}

    private state eventStatus;
    private String title;
    private Date time;
    private String organizerID;

    private List<String> inviteesIDs;

    private EventAttendanceProvider attendanceProvider;

    public EventModel() {
        attendanceProvider = new EventAttendanceProvider(new ArrayList<String>());
    }

    public EventModel(String title, Date time, List<String> inviteesIDs, String organizerID) {
        this.title = title;
        this.time = time;
        eventStatus = state.PENDING;
        this.inviteesIDs = inviteesIDs;
        this.organizerID = organizerID;

        attendanceProvider = new EventAttendanceProvider(inviteesIDs);
    }

    public void setTime(Date newTime) {
        this.time = newTime;
    }

    public state getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(state eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTime() {
        return time;
    }

    public String getOrganizerID() {
        return organizerID;
    }

    public void setOrganizerID(String newID) {
        this.organizerID = newID;
    }

    public List<String> getInviteesIDs() {
        return new ArrayList<>(inviteesIDs);
    }

    public int attendingCount() {
        return attendanceProvider.getAttendingCount();
    }

    public int notAttendingCount() {
        return attendanceProvider.getNotAttendingCount();
    }

    public int tentativesCount() {
        return attendanceProvider.getTentativesCount();
    }

    public int nonResponsiveCount() {
        return attendanceProvider.getNonResponsiveCount();
    }

    public boolean isUserOrganizer(String userID) {
        return this.organizerID.equals(userID);
    }


}
