package com.rshb.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.metrics.MetricRegistries;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.SortedMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
public class ExampleResourceTest {

    @Inject
    MetricRegistries registries;

    @Test
    public void testGetBoundsList() {
        given()
          .when().log().all().get("/rest/api/v1/rshb_bonds")
          .then()
             .statusCode(200)
             .log().all()
             .body("RU000A0ZZ505",equalTo("РСХБ 09Т1"));
        //check metrics

        int getMoexCalls = (int) registries.getApplicationRegistry().meter("com.rshb.example.RestClientMoex.getBondsList").getCount();
        int getBoundsCalls = (int) registries.getApplicationRegistry().meter("com.rshb.example.RshbBounds.getBoundsList").getCount();

        given().when().accept(ContentType.JSON).get("/metrics/")
             .then().statusCode(200)
              .log().all()
                .body("application.'com.rshb.example.RestClientMoex.getBondsList'.count",equalTo(getMoexCalls),
                       "application.'com.rshb.example.RshbBounds.getBoundsList'.count",equalTo(getBoundsCalls));
    }

    @Test
    public void testGetBoundById() {
        given()
                .when().log().all().get("/rest/api/v1/rshb_bonds/RU000A0ZZ505")
                .then()
                .statusCode(200)
                .log().all()
                .body("'Код ценной бумаги'",equalTo("RU000A0ZZ505"));

        int getMoexCalls = (int) registries.getApplicationRegistry().meter("com.rshb.example.RestClientMoex.getBondById").getCount();
        int getBoundsCalls = (int) registries.getApplicationRegistry().meter("com.rshb.example.RshbBounds.getBoundById").getCount();

        given().when().accept(ContentType.JSON).get("/metrics/")
                .then().statusCode(200)
                .log().all()
                .body("application.'com.rshb.example.RestClientMoex.getBondById'.count",equalTo(getMoexCalls),
                        "application.'com.rshb.example.RshbBounds.getBoundById'.count",equalTo(getBoundsCalls));
    }



}

