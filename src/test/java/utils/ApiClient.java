package utils;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiClient {

    private static final String NBP_TABLE_A_URL =
            "https://api.nbp.pl/api/exchangerates/tables/A?format=json";

    public Response getExchangeRatesTableA() {

        return given()
                .relaxedHTTPSValidation()
                .when()
                .get(NBP_TABLE_A_URL);
    }
}