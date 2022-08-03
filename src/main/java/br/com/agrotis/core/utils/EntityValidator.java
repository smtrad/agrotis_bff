package br.com.agrotis.core.utils;

import java.util.Set;
import java.util.function.Consumer;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityValidator {

	@Autowired
	private ValidatorFactory factory;
	
	public <T> Set<ConstraintViolation<T>> getViolations(T model, Class<?> view) {
		Validator validator = factory.getValidator();
		return validator.validate(model, view);
	}
	
	public <T> void validate(T model, Class<?> view, Consumer<ConstraintViolationException> onViolation) {
		Set<ConstraintViolation<T>> violations = getViolations(model, view);
		if (!violations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			violations.stream().forEach(violation -> {
				sb.append(violation.getPropertyPath());
				sb.append(":");
				sb.append(violation.getMessage());
				sb.append(";");
			});			
			ConstraintViolationException cause = new ConstraintViolationException(sb.toString(), violations);
			onViolation.accept(cause);			
		}
	}
}
