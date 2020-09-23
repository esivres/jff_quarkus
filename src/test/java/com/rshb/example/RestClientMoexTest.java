package com.rshb.example;

import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.json.JsonObject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class RestClientMoexTest {

    @Inject
    @RestClient
    RestClientMoex moex;


    @Test
    void checkGetSecurities() {
        JsonObject object = moex.getBondsList("РСХБ").await().indefinitely();
        assertNotNull(object.getJsonObject("securities"));
        assertNotNull(object.getJsonObject("securities").getJsonArray("data"));
    }

    @Test
    void checkGetBoundById() {
        JsonObject object = moex.getBondById("RU000A0JT874").await().indefinitely();
        assertNotNull(object.getJsonObject("description"));
        assertNotNull(object.getJsonObject("description").getJsonArray("data"));
    }


}