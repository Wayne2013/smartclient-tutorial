package com.vel.smartclient.tutorial;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jsonp.JsonProcessingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

public class CountryResourceTest extends JerseyTest {

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		// Create JAX-RS application.
		final Application application = new ResourceConfig()
				.register(JsonProcessingFeature.class)
				.register(CountryResource.class)
				.property(JsonGenerator.PRETTY_PRINTING, true);
		return application;
	}

	@Override
	protected void configureClient(ClientConfig config) {

		ClientBuilder.newClient(new ClientConfig().register(
				JsonProcessingFeature.class).property(
				JsonGenerator.PRETTY_PRINTING, true));

	}

	/**
	 * Test to see that the message "Got it!" is sent in the response.
	 */
	@Test
	public void testGetIt() {
		final JsonObject responseMsg = target().path("country").request()
				.get(JsonObject.class);

		assertNotNull("response is null ", responseMsg.get("response"));
	}

	@Test
	public void testPostIt() {
		Form form = new Form();
		Entity<Form> entity = Entity.form(form);
		final JsonObject responseMsg = target().path("country").request()
				.post(entity, JsonObject.class);

		// assertNotNull("response is null ", responseMsg.get("response"));
	}

	@Test
	public void testPutIt() {
		JsonBuilderFactory factory = Json
				.createBuilderFactory(new HashMap<String, String>());
		JsonObject country = factory
				.createObjectBuilder()
				.add("data",
						factory.createObjectBuilder()
								.add("continent", "ASIA")
								.add("countryName", "212 555-1234")
								.add("countryCode",
										UUID.randomUUID().toString())
								.add("area", 8989).add("population", 9800)
								.add("gdp", 7878)
								.add("independence", "2013-03-01T01:10:00")
								.add("government", "government")
								.add("government_desc", 3)
								.add("capital", "capital")
								.add("member_g8", true)
								.add("article", "article")).build();
		Entity<JsonObject> entity = Entity.json(country);

		final JsonObject responseMsg = target().path("country").request()
				.put(entity, JsonObject.class);

		// assertNotNull("response is null ", responseMsg.get("response"));
	}

	@Test
	public void testDeleteIt() {
		final JsonObject responseMsg = target().path("country")
				.queryParam("countryCode", "1234").request()
				.delete(JsonObject.class);

		// assertNotNull("response is null ", responseMsg.get("response"));
	}
}
