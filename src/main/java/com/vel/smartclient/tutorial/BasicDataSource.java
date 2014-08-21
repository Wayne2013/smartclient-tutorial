package com.vel.smartclient.tutorial;

import javax.json.JsonObject;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

public interface BasicDataSource {

	public JsonObject add(MultivaluedMap<String, String> formParams);

	public JsonObject fetch(@Context UriInfo ui);

	public JsonObject update(final JsonObject document);

	public JsonObject remove(@QueryParam("countryCode") String country);

}
