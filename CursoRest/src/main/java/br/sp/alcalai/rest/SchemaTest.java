package br.sp.alcalai.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.matcher.RestAssuredMatchers. *;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import org.junit.Test;

import org.xml.sax.SAXParseException;

import io.restassured.module.jsv.JsonSchemaValidator;



public class SchemaTest {
	
	@Test
	public void deveValidarSchemaXml() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(200)
			.body(matchesXsdInClasspath("users.xsd"))
			
		;	
	}
	
	@Test(expected=SAXParseException.class)
	public void naoDeveValidarSchemaXml() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/invalidUsersXML")
		.then()
			.log().all()
			.statusCode(200)
			.body(matchesXsdInClasspath("users.xsd"))
			
		;	
	}
	
	@Test
	public void deveValidarSchemaJson() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(200)
			.body(matchesJsonSchemaInClasspath("users.json"))
			
		;	
	}

}
