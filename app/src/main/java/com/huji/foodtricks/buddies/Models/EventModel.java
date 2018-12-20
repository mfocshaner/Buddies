package com.huji.foodtricks.buddies.Models;

import com.huji.foodtricks.buddies.EventAttendanceProvider;

import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;


/**
 * Model of an event, holding its details.
 */
@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class EventModel implements Serializable {

    enum state {PAST, UPCOMING, PENDING}

    private state eventStatus;
    private String title;
    private Date time;
    private String organizerUID;

    private ArrayList<String> inviteesIDs;

    private EventAttendanceProvider attendanceProvider;

    public EventModel() {
        attendanceProvider = new EventAttendanceProvider(new ArrayList<String>());
    }

    public EventModel(String title, Date time, ArrayList<String> inviteesIDs, String organizerUID) {
        this.title = title;
        this.time = time;
        eventStatus = state.PENDING;
        this.inviteesIDs = new ArrayList(inviteesIDs);
        this.organizerUID = organizerUID;

        attendanceProvider = new EventAttendanceProvider(inviteesIDs);
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

    public void setTime(Date time) {
        this.time = time;
    }

    public String getOrganizerUID() {
        return organizerUID;
    }

    public void setOrganizerUID(String organizerUID) {
        this.organizerUID = organizerUID;
    }

    public ArrayList<String> getInviteesIDs() {
        return inviteesIDs;
    }

    public void setInviteesIDs(ArrayList<String> inviteesIDs) {
        this.inviteesIDs = inviteesIDs;
    }

    public EventAttendanceProvider getAttendanceProvider() {
        return attendanceProvider;
    }

    public void setAttendanceProvider(EventAttendanceProvider attendanceProvider) {
        this.attendanceProvider = attendanceProvider;
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
        return this.organizerUID.equals(userID);
    }


}
