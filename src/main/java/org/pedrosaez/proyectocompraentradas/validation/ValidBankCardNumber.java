package org.pedrosaez.proyectocompraentradas.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint( validatedBy = BankCardNumberValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBankCardNumber {

    String message() default "La tarjeta sólo puede ser Visa o Mastercard ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
