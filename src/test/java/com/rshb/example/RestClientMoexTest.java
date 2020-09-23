package com.rshb.example;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.metrics.MetricRegistries;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.json.JsonObject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class RestClientMoexTest {

    @Inject
    @RestClient
    RestClientMoex moex;

    @Inject
    MetricRegistries registries;

    @Test
    void checkGetSecurities() {
        JsonObject object = moex.getBondsList("РСХБ").await().indefinitely();
        System.out.println(object.toString());
        System.err.println(registries.toString());
    }

    @Test
    void checkGetBoundById() {
        JsonObject object = moex.getBondById("RU000A0JT874").await().indefinitely();
        System.out.println(object.toString());
    }


}