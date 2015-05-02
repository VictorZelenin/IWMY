package com.oleksiykovtun.iwmy.speeddating.data;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.io.Serializable;

/**
 * Created by alx on 2015-02-17.
 */
@Entity
@Cache
@Index
@JsonPropertyOrder(alphabetic=true)
public class Event implements Serializable, Comparable<Event> {

    @Id
    private String _eventId;
    private String organizerEmail;
    private String time; // format "2099-12-31 23:59"
    private String place;
    private String streetAddress;
    private String actual;
    private String allowSendingRatings;
    private String maxRatingsPerUser;

    private String photo; // url
    private String thumbnail; // url
    private String freePlaces;
    private String cost;
    private String description;

    public Event() { }

    public Event(String organizerEmail, String time, String place, String streetAddress,
                 String photo, String thumbnail, String freePlaces, String cost, String description) {
        this._eventId = time + "_" + organizerEmail;
        this.organizerEmail = organizerEmail;
        this.place = place;
        this.streetAddress = streetAddress;
        this.time = time;
        this.actual = "true";
        this.allowSendingRatings = "false";
        this.maxRatingsPerUser = "0";
        this.photo = photo;
        this.thumbnail = thumbnail;
        this.freePlaces = freePlaces;
        this.cost = cost;
        this.description = description;
        generateId();
    }

    private void generateId() {
        this._eventId = getTime() + "_" + getOrganizerEmail();
    }

    @Override
    public int compareTo(Event other) {
        return this.get_eventId().compareTo(other.get_eventId());
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Event)
                && this.get_eventId().equals(((Event)other).get_eventId());
    }

    public String get_eventId() {
        return getNotNull(_eventId);
    }

    public void set_eventId(String _eventId) {
        this._eventId = _eventId;
    }

    public String getOrganizerEmail() {
        return getNotNull(organizerEmail);
    }

    public void setOrganizerEmail(String organizerEmail) {
        this.organizerEmail = organizerEmail;
        generateId();
    }

    public String getPlace() {
        return getNotNull(place);
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStreetAddress() {
        return getNotNull(streetAddress);
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getActual() {
        return getNotNull(actual);
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getAllowSendingRatings() {
        return getNotNull(allowSendingRatings);
    }

    public void setAllowSendingRatings(String allowSendingRatings) {
        this.allowSendingRatings = allowSendingRatings;
    }

    public String getMaxRatingsPerUser() {
        return getNotNull(maxRatingsPerUser);
    }

    public void setMaxRatingsPerUser(String maxRatingsPerUser) {
        this.maxRatingsPerUser = maxRatingsPerUser;
    }

    public String getPhoto() {
        return getNotNull(photo);
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getThumbnail() {
        return getNotNull(thumbnail);
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTime() {
        return getNotNull(time);
    }

    public void setTime(String time) {
        this.time = time;
        generateId();
    }

    public String getFreePlaces() {
        return getNotNull(freePlaces);
    }

    public void setFreePlaces(String freePlaces) {
        this.freePlaces = freePlaces;
    }

    public String getCost() {
        return getNotNull(cost);
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return getNotNull(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String getNotNull(String possiblyNullValue) {
        return (possiblyNullValue == null) ? "" : possiblyNullValue;
    }
}
