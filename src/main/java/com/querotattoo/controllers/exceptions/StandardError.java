package com.querotattoo.controllers.exceptions;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class StandardError extends Throwable implements Serializable {
    private static final long serialVersionUID = 1L;
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

    private StandardError() {

    }

    public StandardError(Integer status, String error, String message) {
        super();
        this.timestamp = Instant.now().minus(3, ChronoUnit.HOURS);
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
