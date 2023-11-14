package br.com.danilo.cadastro;

import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.tags.Tags;

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

    @GET
    public List<Restaurante> buscar() {
        return Restaurante.listAll();
    }

    @POST
    @Transactional
    public Response adiicionar(Restaurante restaurante) {
        restaurante.persist();
        return Response.status(Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizar(@PathParam("id") Long id, Restaurante dto) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);

        if (restauranteOp.isEmpty()) {
            throw new NotFoundException();
        }

        Restaurante restaurante = restauranteOp.get();
        restaurante.nome = dto.nome;

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
    public List<Prato> buscarPratos(@PathParam("idRestaurante") Long idRestaurante) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);

        if (restauranteOp.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }
        return Prato.list("restaurante", restauranteOp.get());
    }

    @POST
    @Path("{idRestaurante}/pratos")
    @Tag(name = "prato")
    @Transactional
    public Response adiicionarPrato(@PathParam("idRestaurante") Long idRestaurante, Prato dto) {

        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);

        if (restauranteOp.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }

        Prato prato = new Prato();
        prato.nome = dto.nome;
        prato.descricaco = dto.descricaco;
        prato.preco = dto.preco;
        prato.restaurante = restauranteOp.get();

        prato.persist();
        return Response.status(Status.CREATED).build();
    }

    @PUT
    @Path("{idRestaurante}/pratos/{id}")
    @Tag(name = "prato")
    @Transactional
    public void atualizarPrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id, Prato dto) {

        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);

        if (restauranteOp.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }

        Optional<Prato> pratoOp = Prato.findByIdOptional(id);

        if (pratoOp.isEmpty()) {
            throw new NotFoundException("Prato não encontrado");
        }

        Prato prato = pratoOp.get();
        prato.preco = dto.preco;

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