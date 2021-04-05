package com.nguyenkhanh.backend.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InternationalizationConfig implements WebMvcConfigurer {

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		// set name messages
		messageSource.setBasename("classpath:messages");

		// set UTF-8 for messages_vi.properties
		messageSource.setDefaultEncoding("UTF-8");

		return messageSource;

	}

	@Bean
	public LocalValidatorFactoryBean getValidator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		// register messageSource
		bean.setValidationMessageSource(messageSource());
		return bean;

	}
}
