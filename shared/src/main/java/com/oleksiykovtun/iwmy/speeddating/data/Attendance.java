package com.oleksiykovtun.iwmy.speeddating.data;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.io.Serializable;

/**
 * Created by alx on 2015-02-20.
 */
@Entity
@Cache
@Index
@JsonPropertyOrder(alphabetic=true)
public class Attendance implements Serializable, Comparable<Attendance> {

    @Id
    private String _attendanceId;
    private String userEmail;
    private String username;
    private String userGender;
    private String eventOrganizerEmail;
    private String eventTime; // format "2099-12-31 23:59"

    private String active; // "true" when user is selected to give ratings
    private String creationTime; // long millis

    public Attendance() { }

    public Attendance(User user, Event event) {
        this.eventOrganizerEmail = event.getOrganizerEmail();
        this.eventTime = event.getTime();
        setUser(user);
        this.active = "false";
        this.creationTime = "" + System.currentTimeMillis();
        generateId();
    }

    public void setUser(User user) {
        this.userEmail = user.getEmail();
        this.username = user.getUsername();
        this.userGender = user.getGender();
        generateId();
    }

    private void generateId() {
        this._attendanceId = getEventOrganizerEmail() + "_" + getEventTime() + "_" + getUserEmail();
    }

    @Override
    public int compareTo(Attendance other) {
        return (this.getCreationTime() + this.get_attendanceId()).compareTo(
                other.getCreationTime() + other.get_attendanceId());
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Attendance)
                && (this.getCreationTime() + this.get_attendanceId()).equals(
                ((Attendance)other).getCreationTime() + ((Attendance)other).get_attendanceId());
    }

    public String get_attendanceId() {
        return getNotNull(_attendanceId);
    }

    public void set_attendanceId(String _attendanceId) {
        this._attendanceId = _attendanceId;
    }

    public String getCreationTime() {
        return getNotNull(creationTime);
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
        generateId();
    }

    public String getUserEmail() {
        return getNotNull(userEmail);
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        generateId();
    }

    public String getUserGender() {
        return getNotNull(userGender);
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getEventOrganizerEmail() {
        return getNotNull(eventOrganizerEmail);
    }

    public void setEventOrganizerEmail(String eventOrganizerEmail) {
        this.eventOrganizerEmail = eventOrganizerEmail;
        generateId();
    }

    public String getEventTime() {
        return getNotNull(eventTime);
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
        generateId();
    }

    public String getActive() {
        return getNotNull(active);
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getUsername() {
        return getNotNull(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String getNotNull(String possiblyNullValue) {
        return (possiblyNullValue == null) ? "" : possiblyNullValue;
    }
}
