package com.huji.foodtricks.buddies.Models;

import com.huji.foodtricks.buddies.EventAttendanceProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.io.Serializable;
import java.util.Set;


/**
 * Model of an event, holding its details.
 */
@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class EventModel implements Serializable {

    enum state {PAST, UPCOMING, PENDING}

    private state eventStatus;
    private String title;
    private Date time;
    private String organizerAuthenticationID;

    private Set<String> inviteesIDs;

    private EventAttendanceProvider attendanceProvider;

    public EventModel() {
        attendanceProvider = new EventAttendanceProvider(new ArrayList<String>());
    }

    public EventModel(String title, Date time, List<String> inviteesIDs, String organizerAuthenticationID) {
        this.title = title;
        this.time = time;
        eventStatus = state.PENDING;
        this.inviteesIDs = new HashSet<>(inviteesIDs);
        this.organizerAuthenticationID = organizerAuthenticationID;

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

    public String getOrganizerAuthenticationID() {
        return organizerAuthenticationID;
    }

    public void setOrganizerAuthenticationID(String newID) {
        this.organizerAuthenticationID = newID;
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
        return this.organizerAuthenticationID.equals(userID);
    }

    public EventAttendanceProvider getAttendanceProvider() {
        return attendanceProvider;
    }


}
