package org.pedrosaez.proyectocompraentradas.purchase.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta generada tras procesar una compra de entradas")
public class CustomPurchaseResponseDTO {

    @Schema(
            description = "Mensaje informativo sobre el resultado de la compra",
            example = "Compra realizada correctamente"
    )
    private String mensaje;

    @Schema(
            description = "Nombre del evento para el cual se realizó la compra",
            example = "Concierto de Rock 2026"
    )
    private String evento;

    @Schema(
            description = "Cantidad de entradas adquiridas",
            example = "2"
    )
    private String cantidad;

    @Schema(
            description = "Fecha en la que se realizó la compra",
            example = "2026-03-01T18:30:00"
    )
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
