package hr.rba.rba_it.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OibValidator.class)
public @interface ValidOib {

    String message() default "Value oib is not a valid OIB";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}