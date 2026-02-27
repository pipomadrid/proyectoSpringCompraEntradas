package org.pedrosaez.proyectocompraentradas.purchase.model.response;

public class CustomPurchaseResponseDTO {

    private String mensaje;
    private String evento;
    private String cantidad;
    private String fechaCompra;



    private String clientName;

    public CustomPurchaseResponseDTO(String mensaje, String evento, String amount, String purchaseDate,String clientName) {
        this.mensaje = mensaje;
        this.evento = evento;
        this.cantidad = amount;
        this.fechaCompra = purchaseDate;
        this.clientName = clientName;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String message) {
        this.mensaje = message;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String eventName) {
        this.evento = eventName;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
