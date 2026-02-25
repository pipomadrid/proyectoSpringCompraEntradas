package org.pedrosaez.proyectocompraentradas.purchase.controller.error;

public class EventNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public EventNotFoundException() {
        super("No existe el evento");
    }

    public EventNotFoundException(Long id) {
        super("No existe el evento "+id);
    }
}
