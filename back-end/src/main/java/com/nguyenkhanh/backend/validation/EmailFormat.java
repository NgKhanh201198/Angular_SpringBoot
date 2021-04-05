package com.nguyenkhanh.backend.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = EmailFormatValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailFormat {
	String message() default "Wrong email format";

	String pattern() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
