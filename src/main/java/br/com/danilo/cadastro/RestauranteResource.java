package br.com.danilo.cadastro;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.danilo.cadastro.dto.AdicionarPratoDTO;
import br.com.danilo.cadastro.dto.AdicionarRestauranteDTO;
import br.com.danilo.cadastro.dto.AtualizarPratoDTO;
import br.com.danilo.cadastro.dto.PratoDTO;
import br.com.danilo.cadastro.dto.PratoMapper;
import br.com.danilo.cadastro.dto.RestauranteDTO;
import br.com.danilo.cadastro.dto.RestauranteMapper;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/restaurantes")
@Tag(name = "restaurante")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteResource {

	@Inject
	RestauranteMapper restauranteMapper;
	
	@Inject
	PratoMapper pratoMapper;
	
    @GET
    public List<RestauranteDTO> buscar() {
    	Stream<Restaurante> resturantes = Restaurante.streamAll();
        return resturantes.map(r -> restauranteMapper.toDTO(r)).collect(Collectors.toList());
    }

    @POST
    @Transactional
    public Response adiicionar(AdicionarRestauranteDTO dto) {
    	Restaurante restaurante = restauranteMapper.toRestaurante(dto);
    	restaurante.persist();
        return Response.status(Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizar(@PathParam("id") Long id, AdicionarRestauranteDTO dto) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);

        if (restauranteOp.isEmpty()) {
            throw new NotFoundException();
        }
        
        Restaurante restaurante = restauranteOp.get();
        
        restauranteMapper.toRestaurante(dto, restaurante);

        restaurante.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void deletar(@PathParam("id") Long id) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);

        restauranteOp.ifPresentOrElse(Restaurante::delete, () -> {
            throw new NotFoundException();
        });

    }

    @GET
    @Path("{idRestaurante}/pratos")
    @Tag(name = "prato")
    public List<PratoDTO> buscarPratos(@PathParam("idRestaurante") Long idRestaurante) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);

        if (restauranteOp.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }
        
        Stream<Prato> pratos = Prato.stream("restaurante", restauranteOp.get());
        
        return pratos.map(e -> pratoMapper.toDTO(e)).collect(Collectors.toList());
        
    }

    @POST
    @Path("{idRestaurante}/pratos")
    @Tag(name = "prato")
    @Transactional
    public Response adiicionarPrato(@PathParam("idRestaurante") Long idRestaurante, AdicionarPratoDTO dto) {

        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);

        if (restauranteOp.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }

        Prato prato = pratoMapper.toPrato(dto);
        prato.restaurante = restauranteOp.get();

        prato.persist();
        return Response.status(Status.CREATED).build();
    }

    @PUT
    @Path("{idRestaurante}/pratos/{id}")
    @Tag(name = "prato")
    @Transactional
    public void atualizarPrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id, AtualizarPratoDTO dto) {

        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);

        if (restauranteOp.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }

        Optional<Prato> pratoOp = Prato.findByIdOptional(id);

        if (pratoOp.isEmpty()) {
            throw new NotFoundException("Prato não encontrado");
        }
        
        Prato prato = pratoOp.get();
        pratoMapper.toPrato(dto, prato);
        prato.persist();
    }

    @DELETE
    @Path("{idRestaurante}/pratos/{id}")
    @Tag(name = "prato")
    @Transactional
    public void deletarPrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);

        if (restauranteOp.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }

        Optional<Prato> pratoOp = Prato.findByIdOptional(id);

        pratoOp.ifPresentOrElse(Prato::delete, () -> {
            throw new NotFoundException();
        });

    }

}