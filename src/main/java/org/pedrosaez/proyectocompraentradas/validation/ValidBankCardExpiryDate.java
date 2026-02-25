package org.pedrosaez.proyectocompraentradas.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint( validatedBy = BankCardExpiryDateValidator.class)
@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBankCardExpiryDate {

    String message() default "La tarjeta está caducada";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
