package br.com.danilo.cadastro.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.danilo.cadastro.Restaurante;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {
	
	@Mapping(target = "nome", source = "nomeFantasia")
	Restaurante toRestaurante(AdicionarRestauranteDTO dto);
	
	@Mapping(target = "nome", source = "nomeFantasia")
	void toRestaurante(AdicionarRestauranteDTO dto, @MappingTarget Restaurante restaurante);
	
	@Mapping(target = "nomeFantasia", source = "nome")
	@Mapping(target = "dataCriacao", dateFormat = "yyyy-MM-dd hh:mm:ss")
	RestauranteDTO toDTO(Restaurante restaurante);
	
}
