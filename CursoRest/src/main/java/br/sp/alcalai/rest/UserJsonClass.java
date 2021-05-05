package br.sp.alcalai.rest;

import static io.restassured.RestAssured. *;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserJsonClass {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "https://restapi.wcaquino.me";
		
	}
	
	@Test
	public void deveVerificarPrimeiroNivel() {
		
		given()
		.when()
			.get("/users/1")
		.then()
			.statusCode(200)
			.body("id", is(1))
			.body("name", containsString("Silva"))
			.body("age", greaterThan(18))
		;
	}
	
	@Test
	public void deveVerificarPrimeiroNivelOutrasFormas() {
		
		Response response = request(Method.GET, "/users/1");
		
		//path
		System.out.println(response.path("id"));
		
		// jsonpath
		
		JsonPath jpath = new JsonPath(response.asString());
		Assert.assertEquals(1, jpath.getInt("id"));
	}
	
	@Test
	public void deveVerificarSegundoNivel() {
		
		given()
		.when()
			.get("/users/2")
		.then()
			.statusCode(200)
			.body("name", containsString("Joaquina"))
			.body("endereco.rua", is("Rua dos bobos"))
		;
	}
	
	@Test
	public void devemosVerificarLista() {
		
		given()
		.when()
			.get("/users/3")
		.then()
			.statusCode(200)
			.body("name", containsString("Ana"))
			.body("filhos", hasSize(2))
			.body("filhos[0].name", is("Zezinho"))
			.body("filhos[1].name", is("Luizinho"))
			.body("filhos.name", hasItem("Zezinho"))
			.body("filhos.name", hasItems("Zezinho", "Luizinho"))
		;
	}
	
	@Test
	public void deveVerificarCodErroUserInexistente() {
		
		given()
		.when()
			.get("/users/4")
		.then()
			.statusCode(404)
			.body("error", is("Usuário inexistente"))

		;
	}
	
	@Test
	public void deveVerificarListaRaiz() {
		
		given()
		.when()
			.get("/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
			.body("age[1]", is(25))
			.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))

		;
	}
	
	@Test
	public void devoFazerVerAvançadas() {
		
		given()
		.when()
			.get("/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("age.findAll{it <= 25}.size()", is(2))
			.body("age.findAll{it <= 25 && it > 20}.size()", is(1))
			.body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
			.body("findAll{it.age <= 25}[0].name", is("Maria Joaquina"))
			.body("find{it.age <= 25}.name", is("Maria Joaquina"))
			.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia"))
			.body("findAll{it.name.length() > 10}.name", hasItems("João da Silva", "Maria Joaquina"))
			.body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			.body("id.max()", is(3))
			.body("salary.min()", is(1234.5678f))

		;
	}
	
	@Test
	public void deveSimplificarJsonPath() {

		ArrayList<String> names =

				given()
				.when()
					.get("/users")
				.then()
					.statusCode(200)
					.extract().path("name.findAll{it.startsWith('Maria')}")

		;
		
		Assert.assertEquals(1, names.size());
		Assert.assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
	}

}
