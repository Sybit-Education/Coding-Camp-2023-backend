package de.sybit.sygotchi.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ApiExceptionModel {

    private Date timestamp;

    private String message;

    private int status;

    private String details;

    public ApiExceptionModel(String message, int status, String details) {
        this.timestamp = new Date();
        this.message = message;
        this.status = status;
        this.details = details;
    }
}
