package org.pedrosaez.proyectocompraentradas.purchase.model.response;

import jakarta.validation.constraints.*;
import org.pedrosaez.proyectocompraentradas.validation.ValidBankCardExpiryDate;
import org.pedrosaez.proyectocompraentradas.validation.ValidBankCardNumber;

import java.io.Serializable;

@ValidBankCardExpiryDate
public class BankCardDTO implements Serializable {

    @NotBlank
    @Size(min = 2, max = 100)
    private String ownerName;

    @ValidBankCardNumber
    private String cardNumber;

    @NotNull
    @Min(1)
    @Max(12)
    private Integer expiryMonth;
    private Integer expiryYear;

    @NotBlank
    @Pattern(regexp = "\\d{3}", message = "El CVV debe tener 3 dígitos")
    private String cvv;

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

}
