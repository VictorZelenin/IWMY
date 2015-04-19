package com.oleksiykovtun.iwmy.speeddating.data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.io.Serializable;

/**
 * Created by alx on 2015-02-26.
 */
@Entity
@Index
@JsonPropertyOrder(alphabetic=true)
public class Rating implements Serializable, Comparable<Rating> {

    @Id
    private String _ratingId;
    private String eventOrganizerEmail;
    private String eventTime; // format "2099-12-31 23:59"

    private String thisUserEmail;
    private String otherUserEmail;

    private String number;
    private String selection;
    private String comment;

    private String actual;

    public Rating() { }

    public Rating(String eventOrganizerEmail, String eventTime,
                  String thisUserEmail, String otherUserEmail,
                  String number, String selection, String comment) {
        this._ratingId = number + "_" + eventOrganizerEmail + "_" + eventTime + "_"
                + thisUserEmail + "_" + otherUserEmail;
        this.eventOrganizerEmail = eventOrganizerEmail;
        this.eventTime = eventTime;
        this.thisUserEmail = thisUserEmail;
        this.otherUserEmail = otherUserEmail;
        this.number = number;
        this.selection = selection;
        this.comment = comment;
        this.actual = "false";
    }

    @Override
    public int compareTo(Rating other) {
        return this.get_ratingId().compareTo(other.get_ratingId());
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Rating)
                && this.get_ratingId().equals(((Rating)other).get_ratingId());
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
    }

    public String getThisUserEmail() {
        return "" + thisUserEmail;
    }

    public void setThisUserEmail(String thisUserEmail) {
        this.thisUserEmail = thisUserEmail;
    }

    public String getOtherUserEmail() {
        return "" + otherUserEmail;
    }

    public void setOtherUserEmail(String otherUserEmail) {
        this.otherUserEmail = otherUserEmail;
    }

    public String getNumber() {
        return "" + number;
    }

    public void setNumber(String number) {
        this.number = number;
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
    }

    public String getActual() {
        return "" + actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }
}
