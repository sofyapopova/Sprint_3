import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient {

    private static final String ORDER_PATH = "/api/v1/orders/";

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Отмена заказа")
    public Response cancelOrder(int track) {
        return given()
                .spec(getBaseSpec())
                .body("{\"track\": " + track + "}")
                .when()
                .put(ORDER_PATH + "cancel");
    }

    @Step("Получение списка заказов")
    public Response getOrderList() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH);
    }

    @Step("Принятие заказа")
    public Response acceptOrder(Integer track, Integer courierId) {

        String acceptOrderURI = ORDER_PATH + "accept/";

        if (track != null) {
            acceptOrderURI += track;
        }

        RequestSpecification request = given().spec(getBaseSpec());

        if (courierId != null) {
            request.queryParam("courierId", courierId);
        }

        return request
                .when()
                .put(acceptOrderURI);
    }

    @Step("Получение заказа по его номеру")
    public Response getOrderByTrack(Integer track) {

        RequestSpecification request = given().spec(getBaseSpec());

        if (track != null) {
            request.queryParam("t", track);
        }

        return request
                .when()
                .get(ORDER_PATH + "track");
    }
}
