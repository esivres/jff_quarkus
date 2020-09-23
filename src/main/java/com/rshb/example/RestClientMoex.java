package com.rshb.example;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@RegisterRestClient(configKey = "iss")
@Metered(reusable = true)
@Timed(name = "RestClientMoex.Timing", description = "A measure of how long it takes to perform the primality test.", unit = MetricUnits.MILLISECONDS,reusable = true)
public interface RestClientMoex {

    @GET
    @Path("securities.json")
    Uni<JsonObject> getBondsList(@QueryParam("q") String selector);

    @GET
    @Path("securities/{boundId}.json")
    Uni<JsonObject> getBondById(@PathParam("boundId") String boundId);


}
