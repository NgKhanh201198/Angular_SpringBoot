package com.nguyenkhanh.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MessageProperty {
	@Autowired
	private MessageSource messageSource;

	public String getMessString(String key) {
		return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());

	}
}
