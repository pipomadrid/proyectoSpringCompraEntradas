package org.pedrosaez.proyectocompraentradas.purchase.model.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.BankCardDTO;

import java.io.Serializable;

public class PurchaseRequestDTO implements Serializable {

    private Long id;

    @NotNull
    @Email
    private String email;

    @NotNull
    private Long eventId;

    @Valid
    private BankCardDTO bankCardDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


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

    public BankCardDTO getBankCardDTO() {
        return bankCardDTO;
    }

    public void setBankCardDTO(BankCardDTO bankCardDTO) {
        this.bankCardDTO = bankCardDTO;
    }
}
