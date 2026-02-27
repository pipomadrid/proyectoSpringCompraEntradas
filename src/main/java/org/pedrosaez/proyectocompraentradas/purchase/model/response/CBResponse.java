package org.pedrosaez.proyectocompraentradas.purchase.model.response;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class CBResponse implements Serializable {


    private static final long serialVersionUID = 1L;

    private String message;
    private HttpStatus info;


    public CBResponse() {
        super();
    }

    public CBResponse(String message, HttpStatus info) {
        super();
        this.message = message;
        this.info = info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getInfo() {
        return info;
    }

    public void setInfo(HttpStatus info) {
        this.info = info;
    }

}


