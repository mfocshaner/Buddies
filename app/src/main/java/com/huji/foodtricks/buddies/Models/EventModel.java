package com.huji.foodtricks.buddies.Models;

import com.huji.foodtricks.buddies.EventAttendanceProvider;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;


/**
 * Model of an event, holding its details.
 */
@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class EventModel implements Serializable {

    public enum state {PAST, UPCOMING, PENDING, DELETED}

    private state eventStatus;
    private String title;
    private Date time;
    private String organizerUID;
    private String imageUrl;

    private HashMap<String, String> invitees;

    private EventAttendanceProvider attendanceProvider;

    public EventModel() {
        attendanceProvider = new EventAttendanceProvider(new HashMap<String, String>());
    }

    public EventModel(String title, Date time, HashMap<String, String> invitees, String organizerUID, String imageUrl) {
        this.title = title;
        this.time = time;
        eventStatus = state.PENDING;
        this.invitees = new HashMap<>(invitees);
        this.organizerUID = organizerUID;
        this.imageUrl = imageUrl;

        attendanceProvider = new EventAttendanceProvider(invitees);
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public HashMap<String, String> getInvitees() {
        return invitees;
    }

    public void setInvitees(HashMap<String, String> invitees) {
        this.invitees = invitees;
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
