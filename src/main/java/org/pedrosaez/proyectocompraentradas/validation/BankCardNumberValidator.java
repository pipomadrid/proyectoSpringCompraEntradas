package org.pedrosaez.proyectocompraentradas.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BankCardNumberValidator implements ConstraintValidator<ValidBankCardNumber, String> {

    @Override
    public boolean isValid(String bankCardNumber, ConstraintValidatorContext context) {

        if (bankCardNumber == null || !bankCardNumber.matches("[\\d-]+")) {
            return false;
        }

        // validar VISA
        if (bankCardNumber.startsWith("4") &&
                (bankCardNumber.length() == 13 ||
                        bankCardNumber.length() == 16 ||
                        bankCardNumber.length() == 19)) {
            return true;
        }

        // Validar MASTERCARD
        if (bankCardNumber.length() == 16) {
            int firstTwo = Integer.parseInt(bankCardNumber.substring(0, 2));
            int firstFour = Integer.parseInt(bankCardNumber.substring(0, 4));

            if ((firstTwo >= 51 && firstTwo <= 55) ||
                    (firstFour >= 2221 && firstFour <= 2720)) {
                return true;
            }
        }

        return false;
    }
}
