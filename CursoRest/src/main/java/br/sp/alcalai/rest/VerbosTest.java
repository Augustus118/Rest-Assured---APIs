package br.sp.alcalai.rest;

import static io.restassured.RestAssured. *;
import static org.hamcrest.Matchers. *;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class VerbosTest {
	
	
	@Test
	public void deveSalvarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\":\"Jose\", \"age\":50}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50))
		
		;
	}
	
	@Test
	public void naoDeveSalvarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"age\":50}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"))

		
		;
	}
	
	@Test
	public void deveSalvarUsuarioXml() {
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body("<user><name>Jose</name><age>50</age></user>")
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Jose"))
			.body("user.age", is("50"))
		
		;
	}
	
	@Test
	public void deveAlterarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\":\"Usuario Alterado\", \"age\":80}")
		.when()
			.put("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario Alterado"))
			.body("age", is(80))
		
		;
	}
	
	@Test
	public void deveRemoverUsuario() {
		
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
		
		;
	}
	
	@Test
	public void naoDeveRemoverUsuarioInexistente() {
		
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1000")
		.then()
			.log().all()
			.statusCode(400)
		
		;
	}
	
	@Test
	public void deveSalvarUsuarioComMap() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuario via map");
		params.put("age", 25);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(params)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via map"))
			.body("age", is(25))
		
		;
	}
	
	@Test
	public void deveSalvarUsuarioComObjeto() {
		User user = new User("Usuario via Objeto", 20);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via Objeto"))
			.body("age", is(20))
		
		;
	}
	
	@Test
	public void deveDeserealizarSalvarUsuarioComObjeto() {
		User user = new User("Usuario deserializado", 20);
		
		User usuarioInserido = given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
	     ;
		
		System.out.println(usuarioInserido);
		Assert.assertEquals("Usuario deserializado", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge(), is(20));
		Assert.assertThat(usuarioInserido.getId(), notNullValue());

	}
	
	@Test
	public void deveSalvarUsuarioXmlUsandoObjeto() {
		
		User user = new User("Usuario XML", 50);
		
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Usuario XML"))
			.body("user.age", is("50"))
		
		;
	}
	
	@Test
	public void deveDeserializarSalvarUsuarioXmlUsandoObjeto() {
		
		User user = new User("Usuario XML", 50);
		
		User usuarioInserido = given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
		;

		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals("Usuario XML", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge(), is(40));
	}

}


