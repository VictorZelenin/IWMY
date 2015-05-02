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
public class Couple implements Serializable, Comparable<Couple> {

    @Id
    private String _coupleId;
    private String eventOrganizerEmail;
    private String eventTime;
    private String userEmail1;
    private String userEmail2;

    private String name1;
    private String username1;
    private String birthDate1;
    private String phone1;
    private String thumbnail1;

    private String name2;
    private String username2;
    private String birthDate2;
    private String phone2;
    private String thumbnail2;

    public Couple() { }

    public Couple(Event event, User user1, User user2) {
        setEvent(event);
        setUser1(user1);
        setUser2(user2);
        generateId();
    }

    public void setEvent(Event event) {
        this.eventOrganizerEmail = event.getOrganizerEmail();
        this.eventTime = event.getTime();
        generateId();
    }

    public void setUser1(User user1) {
        this.userEmail1 = user1.getEmail();
        this.username1 = user1.getUsername();
        this.name1 = user1.getNameAndSurname();
        this.phone1 = user1.getPhone();
        this.birthDate1 = user1.getBirthDate();
        this.thumbnail1 = user1.getThumbnail();
        generateId();
    }

    public void setUser2(User user2) {
        this.userEmail2 = user2.getEmail();
        this.username2 = user2.getUsername();
        this.name2 = user2.getNameAndSurname();
        this.phone2 = user2.getPhone();
        this.birthDate2 = user2.getBirthDate();
        this.thumbnail2 = user2.getThumbnail();
        generateId();
    }

    private void generateId() {
        this._coupleId = getEventOrganizerEmail() + "_" + getEventTime()
                + "_" + getUserEmail1() + "_" + getUserEmail2();
    }

    @Override
    public int compareTo(Couple other) {
        return this.get_coupleId().compareTo(other.get_coupleId());
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Couple)
                && this.get_coupleId().equals(((Couple)other).get_coupleId());
    }

    public String get_coupleId() {
        return getNotNull(_coupleId);
    }

    public void set_coupleId(String _coupleId) {
        this._coupleId = _coupleId;
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

    public String getUserEmail1() {
        return getNotNull(userEmail1);
    }

    public void setUserEmail1(String userEmail1) {
        this.userEmail1 = userEmail1;
        generateId();
    }

    public String getUserEmail2() {
        return getNotNull(userEmail2);
    }

    public void setUserEmail2(String userEmail2) {
        this.userEmail2 = userEmail2;
        generateId();
    }

    public String getName1() {
        return getNotNull(name1);
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getBirthDate1() {
        return getNotNull(birthDate1);
    }

    public void setBirthDate1(String birthDate1) {
        this.birthDate1 = birthDate1;
    }

    public String getName2() {
        return getNotNull(name2);
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getBirthDate2() {
        return getNotNull(birthDate2);
    }

    public void setBirthDate2(String birthDate2) {
        this.birthDate2 = birthDate2;
    }

    public String getUsername1() {
        return getNotNull(username1);
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public String getPhone1() {
        return getNotNull(phone1);
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getUsername2() {
        return getNotNull(username2);
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public String getPhone2() {
        return getNotNull(phone2);
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getThumbnail1() {
        return getNotNull(thumbnail1);
    }

    public void setThumbnail1(String thumbnail1) {
        this.thumbnail1 = thumbnail1;
    }

    public String getThumbnail2() {
        return getNotNull(thumbnail2);
    }

    public void setThumbnail2(String thumbnail2) {
        this.thumbnail2 = thumbnail2;
    }

    private String getNotNull(String possiblyNullValue) {
        return (possiblyNullValue == null) ? "" : possiblyNullValue;
    }
}
