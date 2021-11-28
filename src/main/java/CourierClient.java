import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient extends RestAssuredClient {

    private static final String COURIER_PATH = "/api/v1/courier/";

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Авторизация курьера")
    public Response loginCourier(CourierCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(COURIER_PATH + "login");

    }

    @Step("Авторизация курьера и получение его ID")
    public int loginAndGetCourierId(CourierCredentials credentials) {
        return loginCourier(credentials)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @Step("Удаление курьера")
    public Response deleteCourier(int courierId) {
        return given()
                .spec(getBaseSpec())
                .when()
                .delete(COURIER_PATH + courierId);
    }

    @Step("Удаление курьера без указания его ID")
    public Response deleteCourierWithoutCourierId() {
        return given()
                .spec(getBaseSpec())
                .when()
                .delete(COURIER_PATH);
    }

}
