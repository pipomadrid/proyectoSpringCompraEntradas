package org.pedrosaez.proyectocompraentradas.purchase.controller.error;


import org.springframework.http.HttpStatus;

public class PurchaseException extends RuntimeException {


    private final String code;
    private final int status;

    public PurchaseException(String code, String message) {
        super(message);
        this.code = code;
        this.status = HttpStatus.BAD_REQUEST.value();
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
