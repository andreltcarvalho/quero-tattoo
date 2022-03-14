package com.querotattoo.controllers.exceptions;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class StandardErrorMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    private Instant timestamp;
    private String title;
    private String message;
    private String path;

    private StandardErrorMessage() {

    }

    public StandardErrorMessage(String title, String message, String path) {
        super();
        this.timestamp = Instant.now().minus(3, ChronoUnit.HOURS);

        this.title = title;
        this.message = message;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
