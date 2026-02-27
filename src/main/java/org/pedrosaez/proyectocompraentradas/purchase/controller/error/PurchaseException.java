package org.pedrosaez.proyectocompraentradas.purchase.controller.error;

public class PurchaseException extends RuntimeException{

    private final String code;

    public PurchaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
