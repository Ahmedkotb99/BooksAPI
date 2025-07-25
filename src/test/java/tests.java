import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.when;

public class tests {

    public static String BaseURI = "https://simple-books-api.click";
    public static String token;

    @Test
    public void getstatus() {
        RestAssured.given()
                .baseUri(BaseURI)

                .when()
                .get()
                .then()
                .statusCode(200);

    }

    @BeforeMethod
    public static void GenerateToken() {
        Response response = (Response) RestAssured
                .given()
                .baseUri(BaseURI)
                .contentType("application/json")
                .header("Content-Type", "application/json")
                .body("{ \"clientName\": \"Ahmed Qutb\", \"clientEmail\": \"ahmed.api@test.com\" }");
        when()
                .post("/api-clients/")
                .then()
                .statusCode(201)
                .extract().response();

            token = response.jsonPath().getString("accessToken");


    }

    @Test
    public void submitOrder() {
        tests.GenerateToken();
        Response response = RestAssured

                .given()
                .baseUri("https://simple-books-api.click")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("{ \"bookId\": 1, \"customerName\": \"Ahmed Qutb\" }")
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract().response();

    }

    @Test
    public void getOrders() {
        tests.GenerateToken();
        RestAssured.given()
                .baseUri("https://simple-books-api.click")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/orders")
                .then()
                .statusCode(200);
    }

    @Test
    public void updateOrder() {
        tests.GenerateToken();
        RestAssured.given()
                .baseUri("https://simple-books-api.click")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("{ \"orderId\": 1, \"status\": \"complete\" }")
                .when()
                .put("/orders/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void deleteOrder() {
        tests.GenerateToken();
        RestAssured.given()
                .baseUri("https://simple-books-api.click")
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/orders/1")
                .then()
                .statusCode(204);
    }
}


