package org.pedrosaez.proyectocompraentradas.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.pedrosaez.proyectocompraentradas.purchase.model.response.BankCardDTO;

import java.time.YearMonth;

public class BankCardExpiryDateValidator implements ConstraintValidator<ValidBankCardExpiryDate, BankCardDTO> {


    @Override
    public boolean isValid(BankCardDTO bankCard, ConstraintValidatorContext context) {
        if (bankCard.getExpiryMonth() == null || bankCard.getExpiryYear() == null) {
            return false;
        }

        YearMonth expiry = YearMonth.of(
                bankCard.getExpiryYear(),
                bankCard.getExpiryMonth()
        );

        YearMonth now = YearMonth.now();

        return !expiry.isBefore(now);
    }
}
