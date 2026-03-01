package org.pedrosaez.proyectocompraentradas.purchase.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Schema(description = "Respuesta generada tras la activaciónd e Circuit Breaker")
public class CBResponseDTO implements Serializable {


    private static final long serialVersionUID = 1L;

    @Schema(description = "Mensaje descriptivo del error", example = "Servicio temporalmente no disponible ")
    private String message;

    @Schema(description = "Información adicional del error", example = "500 ERROR ")
    private HttpStatus info;


    public CBResponseDTO() {
        super();
    }

    public CBResponseDTO(String message, HttpStatus info) {
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


