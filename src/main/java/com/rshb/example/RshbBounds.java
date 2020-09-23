package com.rshb.example;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("rest/api/v1/rshb_bonds")
@Metered
@Timed(name = "RshbBounds.Timing", description = "A measure of how long it takes to perform the primality test.", unit = MetricUnits.MILLISECONDS)
public class RshbBounds {

    @Inject
    @RestClient
    RestClientMoex moexClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<JsonObject> getBoundsList(){
        return moexClient.getBondsList("РСХБ")
                .map(jsonObject -> jsonObject.getJsonObject("securities").getJsonArray("data"))
                .map(jsonArray->jsonArray.stream())
                .map(stream->stream.map(JsonValue::asJsonArray))
                .map(stream->stream.collect(Json::createObjectBuilder,(builder,array)->builder.add(array.getString(1),array.getString(2)),JsonObjectBuilder::addAll))
                .map(JsonObjectBuilder::build);
    }

    @GET
    @Path("{boundId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<JsonObject> getBoundById(@PathParam("boundId") String boundId){
        return moexClient.getBondById(boundId)
                .map(jsonObject -> jsonObject.getJsonObject("description").getJsonArray("data"))
                .map(jsonArray->jsonArray.stream())
                .map(stream->stream.map(JsonValue::asJsonArray))
                .map(stream->stream.collect(Json::createObjectBuilder,(builder,array)->builder.add(array.getString(1),array.getString(2)),JsonObjectBuilder::addAll))
                .map(JsonObjectBuilder::build);
    }


}