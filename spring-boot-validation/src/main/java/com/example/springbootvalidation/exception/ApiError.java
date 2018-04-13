package com.example.springbootvalidation.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class ApiError {

    private HttpStatus status;
    private String clientMessage;
    private List<FieldError> fieldErrors;
    private List<ObjectError> objectErrors;
    private String developerMessage;
    private ZonedDateTime timestamp;
    private String uuid;
    private String path;

    public ApiError(HttpStatus status, String clientMessage, String developerMessage){
        this.status = status;
        this.clientMessage = clientMessage;
        this.developerMessage = developerMessage;
    }

}
