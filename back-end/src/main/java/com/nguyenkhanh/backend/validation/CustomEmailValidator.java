package com.nguyenkhanh.backend.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomEmailValidator implements ConstraintValidator<CustomEmailValidate, String> {
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
			.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		if (email == null || email.isEmpty()) {
			return false;
		} else {
			Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
			if (matcher.matches()) {
				return true;
			} else {
				return false;
			}
		}
	}

}
