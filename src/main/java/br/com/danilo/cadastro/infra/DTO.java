package br.com.danilo.cadastro.infra;

import jakarta.validation.ConstraintValidatorContext;

public interface DTO {
	
	   default boolean isValid(ConstraintValidatorContext constraintValidatorContext) {
	        return true;
	    }
	
}
