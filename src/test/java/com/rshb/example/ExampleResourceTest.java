package com.rshb.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
public class ExampleResourceTest {

    @Test
    public void testGetBoundsList() {
        given()
          .when().log().all().get("/rshb_bounds")
          .then()
             .statusCode(200)
             .log().all()
             .body("RU000A0ZZ505",equalTo("РСХБ 09Т1"));
        //check metrics
        given().when().accept(ContentType.JSON).get("/metrics/")
             .then().statusCode(200)
              .log().all()
                .body("application.'com.rshb.example.RestClientMoex.getBondsList'.count",equalTo(1),
                       "application.'com.rshb.example.RshbBounds.getBoundsList'.count",equalTo(1));
    }

    @Test
    public void testGetBoundById() {
        given()
                .when().log().all().get("/rshb_bounds/RU000A0ZZ505")
                .then()
                .statusCode(200)
                .log().all()
                .body("'Код ценной бумаги'",equalTo("RU000A0ZZ505"));
        given().when().accept(ContentType.JSON).get("/metrics/")
                .then().statusCode(200)
                .log().all()
                .body("application.'com.rshb.example.RestClientMoex.getBondById'.count",equalTo(1),
                        "application.'com.rshb.example.RshbBounds.getBoundById'.count",equalTo(1));
    }



}

