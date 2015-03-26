package com.oleksiykovtun.iwmy.speeddating.data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.io.Serializable;

/**
 * Created by alx on 2015-02-20.
 */
@Entity
@Index
@JsonPropertyOrder(alphabetic=true)
public class Attendance implements Serializable, Comparable<Attendance> {

    @Id
    private String _attendanceId;
    private String userEmail;
    private String userGender;
    private String eventOrganizerEmail;
    private String eventTime; // format "2099-12-31 23:59"

    private String active; // "true" when user is selected to give ratings

    public Attendance() { }

    public Attendance(String userEmail, String userGender, String eventOrganizerEmail,
                      String eventTime, String active) {
        this._attendanceId = System.currentTimeMillis() + "_" + userEmail + "_"
                + eventOrganizerEmail + "_" + eventTime; // comparable by time of creation
        this.userEmail = userEmail;
        this.userGender = userGender;
        this.eventOrganizerEmail = eventOrganizerEmail;
        this.eventTime = eventTime;
        this.active = active;
    }

    public Attendance(User user, Event event) {
        this._attendanceId = user.getEmail() + "_" + event.getOrganizerEmail() + "_" + event.getTime();
        this.userEmail = user.getEmail();
        this.userGender = user.getGender();
        this.eventOrganizerEmail = event.getOrganizerEmail();
        this.eventTime = event.getTime();
        this.active = "false";
    }

    @Override
    public int compareTo(Attendance other) {
        return this.get_attendanceId().compareTo(other.get_attendanceId());
    }

    public String get_attendanceId() {
        return "" + _attendanceId;
    }

    public void set_attendanceId(String _attendanceId) {
        this._attendanceId = _attendanceId;
    }

    public String getUserEmail() {
        return "" + userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserGender() {
        return "" + userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getEventOrganizerEmail() {
        return "" + eventOrganizerEmail;
    }

    public void setEventOrganizerEmail(String eventOrganizerEmail) {
        this.eventOrganizerEmail = eventOrganizerEmail;
    }

    public String getEventTime() {
        return "" + eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getActive() {
        return "" + active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
