package org.pedrosaez.proyectocompraentradas.purchase.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.pedrosaez.proyectocompraentradas.validation.ValidBankCardExpiryDate;
import org.pedrosaez.proyectocompraentradas.validation.ValidBankCardNumber;

import java.io.Serializable;

@Schema(description = "Información de la tarjeta bancaria utilizada para realizar el pago")
@ValidBankCardExpiryDate
public class BankCardRequestDTO implements Serializable {

    @NotBlank
    @Size(min = 2, max = 100)
    @Schema(
            description = "Nombre del titular de la tarjeta",
            example = "Manolito Rodriguez",
            minLength = 2,
            maxLength = 100
    )
    private String ownerName;

    @ValidBankCardNumber
    @Schema(
            description = "Número de la tarjeta bancaria",
            example = "4111-1111-1111-1111"
    )
    private String cardNumber;

    @NotNull
    @Min(1)
    @Max(12)
    @Schema(
            description = "Mes de expiración de la tarjeta",
            example = "12",
            minimum = "1",
            maximum = "12"
    )
    private Integer expiryMonth;

    @Schema(
            description = "Año de expiración de la tarjeta",
            example = "2028"
    )
    private Integer expiryYear;

    @NotBlank
    @Pattern(regexp = "\\d{3}", message = "El CVV debe tener 3 dígitos")
    @Schema(
            description = "Código de seguridad CVV (3 dígitos)",
            example = "123",
            pattern = "\\d{3}"
    )
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
