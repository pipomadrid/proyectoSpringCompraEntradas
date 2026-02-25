package org.pedrosaez.proyectocompraentradas.purchase.model.response;

import jakarta.persistence.*;
import org.pedrosaez.proyectocompraentradas.purchase.model.BankCard;

import java.io.Serializable;

public class PurchaseDTO implements Serializable {

    private Long id;
    private String email;
    private Long eventId;
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

    public BankCardDTO getBankCard() {
        return bankCardDTO;
    }

    public void setBankCard(BankCardDTO bankCardDTO) {
        this.bankCardDTO = bankCardDTO;
    }
}
