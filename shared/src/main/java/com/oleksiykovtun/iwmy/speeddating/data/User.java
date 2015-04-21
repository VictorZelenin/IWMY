package com.oleksiykovtun.iwmy.speeddating.data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonRawValue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alx on 2015-02-17.
 */
@Entity
@Index
@JsonPropertyOrder(alphabetic=true)
public class User implements Serializable, Comparable<User> {

    public static final String USER = "user";
    public static final String ORGANIZER = "organizer";
    public static final String MALE = "male";
    public static final String FEMALE = "female";

    @Id
    private String _userId;
    private String email;
    private String password;
    private String username;
    private String group;

    private String nameAndSurname;
    private String photoBase64; // todo url
    private String phone;
    private String birthDate; // format "2099-12-31"

    private String gender;
    private String orientation;
    private String goal;
    private String affair;

    private String height;
    private String weight;
    private String attitudeToSmoking;
    private String attitudeToAlcohol;

    private String location;
    private String organization;
    private String website;
    private String isChecked;

    public User() { }

    public User(String email) {
        this._userId = "" + email;
        this.email = email;
    }

    public User(String email, String password, String username, String group,
                String nameAndSurname, String photoBase64, String phone, String birthDate,
                String gender, String orientation, String goal, String affair,
                String height, String weight, String attitudeToSmoking, String attitudeToAlcohol,
                String location, String organization, String website) {
        this.group = group;
        this.username = username;
        this.email = email;
        this.password = password;
        this.nameAndSurname = nameAndSurname;
        this.photoBase64 = photoBase64;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
        this.orientation = orientation;
        this.goal = goal;
        this.affair = affair;
        this.height = height;
        this.weight = weight;
        this.attitudeToSmoking = attitudeToSmoking;
        this.attitudeToAlcohol = attitudeToAlcohol;
        this.location = location;
        this.organization = organization;
        this.website = website;
        this.isChecked = "";
        generateId();
    }

    private void generateId() {
        this._userId = getNameAndSurname() + "_" + getEmail();
    }

    @Override
    public int compareTo(User other) {
        return this.get_userId().compareTo(other.get_userId());
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof User)
                && this.get_userId().equals(((User)other).get_userId());
    }

    public String get_userId() {
        return "" + _userId;
    }

    public void set_userId(String _userId) {
        this._userId = _userId;
    }

    public String group() {
        return "" + group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUsername() {
        return "" + username;
    }

    public void setUsername(String username) {
        this.username = username;
        generateId();
    }

    public String getEmail() {
        return "" + email;
    }

    public void setEmail(String email) {
        this.email = email;
        generateId();
    }

    public String getPassword() {
        return "" + password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNameAndSurname() {
        return "" + nameAndSurname;
    }

    public void setNameAndSurname(String nameAndSurname) {
        this.nameAndSurname = nameAndSurname;
    }

    public String getPhotoBase64() {
        return "" + photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public String getPhone() {
        return "" + phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthDate() {
        return "" + birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return "" + gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOrientation() {
        return "" + orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getGoal() {
        return "" + goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getAffair() {
        return "" + affair;
    }

    public void setAffair(String affair) {
        this.affair = affair;
    }

    public String getHeight() {
        return "" + height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return "" + weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAttitudeToSmoking() {
        return "" + attitudeToSmoking;
    }

    public void setAttitudeToSmoking(String attitudeToSmoking) {
        this.attitudeToSmoking = attitudeToSmoking;
    }

    public String getAttitudeToAlcohol() {
        return "" + attitudeToAlcohol;
    }

    public void setAttitudeToAlcohol(String attitudeToAlcohol) {
        this.attitudeToAlcohol = attitudeToAlcohol;
    }

    public String getGroup() {
        return "" + group;
    }

    public String getLocation() {
        return "" + location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrganization() {
        return "" + organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getWebsite() {
        return "" + website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIsChecked() {
        return "" + isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }
}
