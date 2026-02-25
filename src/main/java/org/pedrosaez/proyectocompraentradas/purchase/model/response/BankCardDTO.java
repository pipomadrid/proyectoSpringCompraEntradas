package org.pedrosaez.proyectocompraentradas.purchase.model.response;

import java.io.Serializable;

public class BankCardDTO implements Serializable {


    private Long id;
    private String ownerName;
    private String cardNumber;
    private Integer expiryMonth;
    private Integer expiryYear;
    private String cvv;
    private PurchaseDTO purchaseDTO;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(Integer expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public Integer getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(Integer expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public PurchaseDTO getPurchaseDTO() {
        return purchaseDTO;
    }

    public void setPurchaseDTO(PurchaseDTO purchaseDTO) {
        this.purchaseDTO = purchaseDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
