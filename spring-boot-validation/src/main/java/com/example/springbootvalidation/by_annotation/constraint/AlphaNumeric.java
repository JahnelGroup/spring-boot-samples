package com.example.springbootvalidation.by_annotation.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AlphaNumericConstraint.class)
public @interface AlphaNumeric {

    String message() default "{AlphaNumeric.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     *
     *
     * @return minimum numbers of digits required
     */
    int minDigits() default 2;

    /**
     * @return minimum numbers of letters required
     */
    int minLetters() default 2;

}
