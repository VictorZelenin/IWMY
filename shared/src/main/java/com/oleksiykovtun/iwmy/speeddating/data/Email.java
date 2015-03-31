package com.oleksiykovtun.iwmy.speeddating.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.io.Serializable;

/**
 * Created by alx on 2015-02-26.
 */
@JsonPropertyOrder(alphabetic=true)
public class Email implements Serializable {

    public static transient final String VALIDATION_REGEX = "^[_A-Za-z0-9-\\+]+" +
            "(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private String fromAddress;
    private String fromName;
    private String toAddress;
    private String toName;
    private String subject;
    private String message;

    public Email() { }

    public Email(String fromAddress, String fromName, String toAddress, String toName,
                 String subject, String message) {
        this.fromAddress = fromAddress;
        this.fromName = fromName;
        this.toAddress = toAddress;
        this.toName = toName;
        this.subject = subject;
        this.message = message;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
