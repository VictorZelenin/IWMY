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
public class Thumbnail implements Serializable {

    @Id
    private String _id;

    private String path;
    private byte[] binaryData;

    public Thumbnail() { }

    public Thumbnail(String path, byte[] binaryData) {
        this.path = path;
        this.binaryData = binaryData;
        generateId();
    }

    private void generateId() {
        this._id = System.currentTimeMillis() + "_" + getPath();
    }

    public String getPath() {
        return getNotNull(path);
    }

    public void setPath(String path) {
        this.path = path;
        generateId();
    }

    public String get_id() {
        return getNotNull(_id);
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    private String getNotNull(String possiblyNullValue) {
        return (possiblyNullValue == null) ? "" : possiblyNullValue;
    }
}
