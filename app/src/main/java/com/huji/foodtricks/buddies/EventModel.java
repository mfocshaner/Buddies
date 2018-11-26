package com.huji.foodtricks.buddies;

import java.util.Date;
import java.util.List;

/**
 * Model of an event, holding its details.
 */
public class EventModel {

    enum state { PAST, UPCOMING, PENDING }

    private state eventStatus;
    private String title;
    private Date time;

    private String eventType; // not well defined
    private List<String> invitees; // will maybe change if invitees become "People" objects,
    /// or if we create a "PeopleGroup" object.

    /////// Note: we probably want to have a notification sent to all invitees when any detail of
    // the event changes; this may be needed here, or in a higher "event controller".
    // should be discussed.

    public EventAttendanceProvider attendanceProvider;

    public EventModel(String title, Date time, String eventType, List<String> invitees) {
        this.title = title;
        this.time = time;
        this.eventType = eventType;
        eventStatus = state.PENDING;

        attendanceProvider = new EventAttendanceProvider(invitees);
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getAttendingCount(){
        return attendanceProvider.getAttendingCount();
    }

    public int getNotAttendingCount(){
        return attendanceProvider.getNotAttendingCount();
    }
    public int getTentativesCount(){
        return attendanceProvider.getTentativesCount();
    }

    public int getNonResponsiveCount(){
        return attendanceProvider.getNonResponsiveCount();
    }



}
