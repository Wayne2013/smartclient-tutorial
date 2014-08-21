package com.vel.smartclient.tutorial;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

/**
 * Root resource (exposed at "country" path)
 */
@Path("country")
public class CountryResource implements BasicDataSource {

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to
	 * the client as "text/plain" media type.
	 * 
	 * @return String that will be returned as a text/plain response.
	 */
	private static Map<String, JsonObject> inMemoryMap = new HashMap<String, JsonObject>();

	static {
		JsonBuilderFactory factory = Json
				.createBuilderFactory(new HashMap<String, String>());
		for (int i = 0; i < 10; i++) {
			String uuid = UUID.randomUUID().toString();
			JsonObject country = factory.createObjectBuilder()
					.add("continent", "ASIA" + i)
					.add("countryName", "212 555-1234" + i)
					.add("countryCode", uuid).add("population", 9800 + i)
					.add("independence", "2013-03-01T01:10:00" + i)
					.add("government", "government" + i)
					.add("government_desc", 3 + i)
					.add("capital", "capital" + i).add("member_g8", true)
					.add("article", "article" + i).build();
			inMemoryMap.put(uuid, country);
		}

	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject add(MultivaluedMap<String, String> formParams) {
		JsonBuilderFactory factory = Json
				.createBuilderFactory(new HashMap<String, String>());
		System.out.println("*Request Received*" + formParams);
		if (!formParams.isEmpty()) {
			String uuid = UUID.randomUUID().toString();
			JsonObject country = factory
					.createObjectBuilder()
					.add("continent",
							formParams.getFirst("continent") == null ? ""
									: formParams.getFirst("continent"))
					.add("countryName",
							formParams.getFirst("countryName") == null ? ""
									: formParams.getFirst("countryName"))
					.add("countryCode",
							formParams.getFirst("countryCode") == null ? uuid
									: formParams.getFirst("countryCode"))
					.add("population",
							formParams.getFirst("population") == null ? ""
									: formParams.getFirst("population"))
					.add("independence",
							formParams.getFirst("independence") == null ? ""
									: formParams.getFirst("independence"))
					.add("government",
							formParams.getFirst("government") == null ? ""
									: formParams.getFirst("government"))
					.add("government_desc",
							formParams.getFirst("government_desc") == null ? ""
									: formParams.getFirst("government_desc"))
					.add("capital",
							formParams.getFirst("capital") == null ? ""
									: formParams.getFirst("capital"))
					.add("member_g8",
							formParams.getFirst("member_g8") == null ? ""
									: formParams.getFirst("member_g8"))
					.add("article",
							formParams.getFirst("article") == null ? ""
									: formParams.getFirst("article")).build();

			inMemoryMap.put(uuid, country);
			System.out.println("*country added to the inmemory database*");
			JsonObject jsonObject = factory
					.createObjectBuilder()
					.add("response",
							factory.createObjectBuilder()
									.add("status", 0)
									.add("data",
											factory.createArrayBuilder().add(
													country))).build();

			System.out.print(" * Response is constructed * "
					+ jsonObject.toString());
			return jsonObject;
		} else {
			return factory.createObjectBuilder().build();
		}

	}

	@Override
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject fetch(@Context UriInfo ui) {
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		System.out.println(queryParams.values());
		JsonBuilderFactory factory = Json
				.createBuilderFactory(new HashMap<String, String>());
		JsonArrayBuilder createArrayBuilder = factory.createArrayBuilder();
		JsonObjectBuilder responseObjectBuilder = factory.createObjectBuilder();

		for (Entry<String, JsonObject> key : inMemoryMap.entrySet()) {
			createArrayBuilder.add(inMemoryMap.get(key.getKey()));
		}

		JsonObjectBuilder objectBuilder = responseObjectBuilder.add(
				"response",
				responseObjectBuilder.add("status", 0).add("startRow", 0)
						.add("endRow", inMemoryMap.size())
						.add("totalRows", inMemoryMap.size())
						.add("data", createArrayBuilder.build()));
		return objectBuilder.build();
	}

	@Override
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject update(JsonObject document) {
		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		System.out.println("* Request received *" + document);
		JsonObjectBuilder updatedJsonBuilder = Json.createObjectBuilder();
		JsonObject jsonObject = document.getJsonObject("data");
		String countryCode = jsonObject.getString(("countryCode"));
		System.out.println(" *Country code is *" + countryCode);
		System.out.println("* The inmemory map is --> " + inMemoryMap + "*");
		if (inMemoryMap.containsKey(countryCode)) {
			JsonObject originalCountyObject = inMemoryMap.get(countryCode);
			JsonValue continent = jsonObject.get("continent");
			if (continent != null) {
				updatedJsonBuilder.add("continent", continent);
			} else {
				updatedJsonBuilder.add("continent",
						originalCountyObject.get("continent"));
			}
			JsonValue countryName = jsonObject.get("countryName");
			if (countryName != null) {
				updatedJsonBuilder.add("countryName", countryName);
			} else {
				updatedJsonBuilder.add("countryName",
						originalCountyObject.get("countryName"));
			}
			JsonValue population = jsonObject.get("population");
			if (population != null) {
				updatedJsonBuilder.add("population", population);
			} else {
				updatedJsonBuilder.add("population",
						originalCountyObject.get("population"));
			}
			JsonValue independence = jsonObject.get("independence");
			if (independence != null) {
				updatedJsonBuilder.add("independence", independence);
			} else {
				updatedJsonBuilder.add("independence",
						originalCountyObject.get("independence"));
			}
			JsonValue government = jsonObject.get("government");
			if (government != null) {
				updatedJsonBuilder.add("government", government);
			} else {
				updatedJsonBuilder.add("government",
						originalCountyObject.get("government"));
			}
			JsonValue government_desc = jsonObject.get("government_desc");
			if (government_desc != null) {
				updatedJsonBuilder.add("government_desc", government_desc);
			} else {
				updatedJsonBuilder.add("government_desc",
						originalCountyObject.get("government_desc"));
			}
			JsonValue capital = jsonObject.get("capital");
			if (capital != null) {
				updatedJsonBuilder.add("capital", capital);
			} else {
				updatedJsonBuilder.add("capital",
						originalCountyObject.get("capital"));
			}
			JsonValue member_g8 = jsonObject.get("member_g8");
			if (member_g8 != null) {
				updatedJsonBuilder.add("member_g8", member_g8);
			} else {
				updatedJsonBuilder.add("member_g8",
						originalCountyObject.get("member_g8"));
			}
			JsonValue article = jsonObject.get("article");
			if (article != null) {
				updatedJsonBuilder.add("article", article);
			} else {
				updatedJsonBuilder.add("article",
						originalCountyObject.get("article"));
			}
			JsonObject updatedJsonObject = updatedJsonBuilder.add(
					"countryCode", countryCode).build();
			inMemoryMap.put(countryCode, updatedJsonObject);
			jsonObjectBuilder.add(
					"response",
					Json.createObjectBuilder()
							.add("status", 0)
							.add("data",
									Json.createArrayBuilder().add(
											updatedJsonObject)));
			System.out.println("* Country updated for  *" + countryCode);
			return jsonObjectBuilder.build();
		} else {
			System.out.println("NOT_UPDATED");
			return jsonObjectBuilder.build();
		}
	}

	@Override
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject remove(@QueryParam("countryCode") String country) {
		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		System.out.println("* Request received --> " + country + "*");
		if (inMemoryMap.containsKey(country)) {
			inMemoryMap.remove(country);
			jsonObjectBuilder.add(
					"response",
					jsonObjectBuilder.add("status", 0).add(
							"data",
							Json.createArrayBuilder().add(
									jsonObjectBuilder.add("countryCode",
											country))));
			System.out.println("Country removed " + country);
			return jsonObjectBuilder.build();
		} else {
			System.out.println("country is not available to delete");
			return jsonObjectBuilder.build();
		}
	}

}
