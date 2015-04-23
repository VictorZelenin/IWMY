package com.oleksiykovtun.iwmy.speeddating.data;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.io.Serializable;

/**
 * Created by alx on 2015-02-26.
 */
@Entity
@Cache
@Index
@JsonPropertyOrder(alphabetic=true)
public class Rating implements Serializable, Comparable<Rating> {

    public static final String SELECTED = "selected";
    public static final String NOT_SELECTED = "";

    @Id
    private String _ratingId;
    private String eventOrganizerEmail;
    private String eventTime; // format "2099-12-31 23:59"

    private String thisUserEmail;
    private String otherUserEmail;

    private String number;
    private String username;
    private String selection;
    private String comment;

    private String actual;

    public Rating() { }

    public Rating(String eventOrganizerEmail, String eventTime,
                  String thisUserEmail, String otherUserEmail,
                  String number, String username, String selection, String comment) {
        this.eventOrganizerEmail = eventOrganizerEmail;
        this.eventTime = eventTime;
        this.thisUserEmail = thisUserEmail;
        this.otherUserEmail = otherUserEmail;
        this.number = number;
        this.username = username;
        this.selection = selection;
        this.comment = comment;
        this.actual = "false";
        generateId();
    }

    private void generateId() {
        this._ratingId = getEventOrganizerEmail() + "_" + getEventTime()
                + "_" + getThisUserEmail() + "_" + getOtherUserEmail();
    }

    @Override
    public int compareTo(Rating other) {
        return (this.getNumberWithLeadingZeros() + this.get_ratingId()).compareTo(
                other.getNumberWithLeadingZeros() + other.get_ratingId());
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Rating)
                && (this.getNumberWithLeadingZeros() + this.get_ratingId()).equals(
                ((Rating)other).getNumberWithLeadingZeros() + ((Rating)other).get_ratingId());
    }

    private String getNumberWithLeadingZeros() {
        return ("000" + getNumber()).substring(getNumber().length());
    }

    public String get_ratingId() {
        return "" + _ratingId;
    }

    public void set_ratingId(String _ratingId) {
        this._ratingId = _ratingId;
    }

    public String getEventOrganizerEmail() {
        return "" + eventOrganizerEmail;
    }

    public void setEventOrganizerEmail(String eventOrganizerEmail) {
        this.eventOrganizerEmail = eventOrganizerEmail;
        generateId();
    }

    public String getThisUserEmail() {
        return "" + thisUserEmail;
    }

    public void setThisUserEmail(String thisUserEmail) {
        this.thisUserEmail = thisUserEmail;
        generateId();
    }

    public String getOtherUserEmail() {
        return "" + otherUserEmail;
    }

    public void setOtherUserEmail(String otherUserEmail) {
        this.otherUserEmail = otherUserEmail;
        generateId();
    }

    public String getNumber() {
        return "" + number;
    }

    public void setNumber(String number) {
        this.number = number;
        generateId();
    }

    public String getUsername() {
        return "" + username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSelection() {
        return "" + selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String getComment() {
        return "" + comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEventTime() {
        return "" + eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
        generateId();
    }

    public String getActual() {
        return "" + actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }
}
