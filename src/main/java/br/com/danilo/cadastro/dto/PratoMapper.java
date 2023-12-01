package br.com.danilo.cadastro.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import br.com.danilo.cadastro.Prato;

@Mapper(componentModel = "cdi")
public interface PratoMapper {
	
	Prato toPrato(AdicionarPratoDTO dto);
	
	void toPrato(AdicionarPratoDTO dto, @MappingTarget Prato prato);
	
	PratoDTO toDTO(Prato prato);
	
}
