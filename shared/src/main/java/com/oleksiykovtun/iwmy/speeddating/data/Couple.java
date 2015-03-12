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
public class Couple implements Serializable, Comparable<Couple> {

    @Id
    private String _coupleId;
    private String eventOrganizerEmail;
    private String eventTime;
    private String userEmail1;
    private String userEmail2;

    private String name1;
    private String birthDate1;
    private String name2;
    private String birthDate2;

    public Couple() { }

    public Couple(String eventOrganizerEmail, String eventTime, String userEmail1, String userEmail2,
                  String name1, String birthDate1, String name2, String birthDate2) {
        this._coupleId = eventOrganizerEmail + "_" + eventTime + "_" + userEmail1 + "_" + userEmail2;
        this.eventOrganizerEmail = eventOrganizerEmail;
        this.eventTime = eventTime;
        this.userEmail1 = userEmail1;
        this.userEmail2 = userEmail2;
        this.name1 = name1;
        this.birthDate1 = birthDate1;
        this.name2 = name2;
        this.birthDate2 = birthDate2;
    }

    @Override
    public int compareTo(Couple other) {
        return this.get_coupleId().compareTo(other.get_coupleId());
    }

    public String get_coupleId() {
        return "" + _coupleId;
    }

    public void set_coupleId(String _coupleId) {
        this._coupleId = _coupleId;
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

    public String getUserEmail1() {
        return "" + userEmail1;
    }

    public void setUserEmail1(String userEmail1) {
        this.userEmail1 = userEmail1;
    }

    public String getUserEmail2() {
        return "" + userEmail2;
    }

    public void setUserEmail2(String userEmail2) {
        this.userEmail2 = userEmail2;
    }

    public String getName1() {
        return "" + name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getBirthDate1() {
        return "" + birthDate1;
    }

    public void setBirthDate1(String birthDate1) {
        this.birthDate1 = birthDate1;
    }

    public String getName2() {
        return "" + name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getBirthDate2() {
        return "" + birthDate2;
    }

    public void setBirthDate2(String birthDate2) {
        this.birthDate2 = birthDate2;
    }
}
