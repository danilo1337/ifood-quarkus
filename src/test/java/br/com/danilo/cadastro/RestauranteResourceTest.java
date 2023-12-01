package br.com.danilo.cadastro;

import static io.restassured.RestAssured.given;

import org.approvaltests.Approvals;
import org.junit.Test;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.dataset.DataSet;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@DBRider
@QuarkusTest
@QuarkusTestResource(CadastroTestLifeCycleManager.class)
public class RestauranteResourceTest {

	@Test
	@DataSet("restaurantes-cenario-1.yml")
	public void testBuscarRestaurantes() {
		String resultado = given()
				.when().get("/restaurantes")
				.then()
				.statusCode(200)
				.extract().asString();
		System.out.println(resultado);
		Approvals.verifyJson(resultado);
	}

}