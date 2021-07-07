package br.sp.alcalai.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers. *;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;



public class StarWarsApi {
	
	@Test
	public void deveAcessarSWAPI() {
		
		given()
			.log().all()
		.when()
			.get("https://swapi.dev/api/people/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Luke Skywalker"))
		;
	}
	
	@Test
	public void devoObterClima() {
		
		given()
			.log().all()
			.queryParam("q", "Sao Paulo, BR")
			.queryParam("appid", "77038ac0bebf3fe19159113c2f9a6f2d")
			.queryParam("units", "metric")
		.when()
			.get("http://api.openweathermap.org/data/2.5/weather")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("São Paulo"))
			.body("coord.lon", is(-46.6361f))

		;
	}
	
	//77038ac0bebf3fe19159113c2f9a6f2d
	//api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}&units=metric

	//api.openweathermap.org/data/2.5/weather?q=Sao Paulo, BR &appid=77038ac0bebf3fe19159113c2f9a6f2d
	
	@Test
	public void naodeveAcessarSemSenha() {
		
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401)

		;
	}
	
	@Test
	public void deveFazerAutenticacao() {
		
		given()
			.log().all()
		.when()
			.get("https://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))

		;
	}
	
	@Test
	public void deveFazerAutenticacao2() {
		
		given()
			.log().all()
			.auth().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))

		;
	}
	
	@Test
	public void deveFazerAutenticacaoChallenge() {
		
		given()
			.log().all()
			.auth().preemptive().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth2")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))

		;
	}
	
	@Test
	public void deveFazerAutenticacaoTokenJwt() {
		
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "abernardes@deloitte.com");
		login.put("senha", "123456");
		
		// Login na api
		// Receber o token
		String token = given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("http://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token")

		;
						
		// Obter as contas
		
		given()
			.log().all()
			.header("Authorization", "JWT " + token)
		.when()
			.get("http://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", hasItem("Conta de Teste"))

	;
		
	}

}
