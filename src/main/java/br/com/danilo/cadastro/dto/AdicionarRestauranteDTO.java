package br.com.danilo.cadastro.dto;

import br.com.danilo.cadastro.Restaurante;
import br.com.danilo.cadastro.infra.DTO;
import br.com.danilo.cadastro.infra.ValidDTO;
import io.smallrye.common.constraint.NotNull;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@ValidDTO
public class AdicionarRestauranteDTO implements DTO {

	@NotEmpty
	@NotNull
	public String propietario;

	@Pattern(regexp = "[0-9]{2}\\.[0-9]{3}\\.[0-9]{3}\\/[0-9]{4}\\-[0-9]{2}")
	@NotNull
	public String cnpj;

	@Size(min = 3, max = 30)
	public String nomeFantasia;

	public LocalizacaoDTO localizacao;
	
	@Override
	public boolean isValid(ConstraintValidatorContext constraintValidatorContext) {

		constraintValidatorContext.disableDefaultConstraintViolation();
		
		if (Restaurante.find("cnpj", cnpj).count() > 0) {
			constraintValidatorContext.buildConstraintViolationWithTemplate("CNPJ já cadastrado")
									  .addPropertyNode("cnpj")
									  .addConstraintViolation();
			return false;
		}

		return true;
	}

}
