package org.pedrosaez.proyectocompraentradas.purchase.model.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Schema(description = "Datos necesarios para procesar una compra de entradas")
public class PurchaseRequestDTO implements Serializable {


    @NotNull
    @Email
    @Schema(
            description = "Correo electrónico del comprador",
            example = "usuario@email.com"
    )
    private String email;

    @NotNull
    @Schema(
            description = "Identificador del evento para el cual se realiza la compra",
            example = "200"
    )
    private Long eventId;

    @Valid
    @Schema(
            description = "Datos de la tarjeta bancaria utilizada para el pago"
    )
    private BankCardRequestDTO bankCardDTO;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public BankCardRequestDTO getBankCardDTO() {
        return bankCardDTO;
    }

    public void setBankCardDTO(BankCardRequestDTO bankCardDTO) {
        this.bankCardDTO = bankCardDTO;
    }
}
