import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DisplayName("Тесты на подтверждение заказа")
public class OrderAcceptanceTest {

    private OrderClient orderClient;
    private CourierClient courierClient;
    private int courierId;
    private int track;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        courierClient = new CourierClient();

        track = orderClient.createOrder(Order.getRandom())
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("track");

        Courier courier = new CourierBuilder().random().buildCourier();
        courierClient.createCourier(courier);
        courierId = courierClient.loginAndGetCourierId(CourierCredentials.from(courier));
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
        orderClient.cancelOrder(track)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void checkOrderCanBeAccepted() {

        boolean isOrderAccepted = orderClient.acceptOrder(track, courierId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("ok");

        assertTrue("Order is not accepted.", isOrderAccepted);
    }

    @Test
    public void checkErrorWhenTryingToAcceptOrderWithoutTrack() {

        String actualResponseMessage = orderClient.acceptOrder(null, courierId)
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        String expectedResponseMessage = "Недостаточно данных для поиска";

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

    @Test
    public void checkErrorWhenTryingToAcceptOrderWithoutCourierId() {

        String actualResponseMessage = orderClient.acceptOrder(track, null)
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        String expectedResponseMessage = "Недостаточно данных для поиска";

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

    @Test
    public void checkErrorWhenTryingToAcceptOrderWithIncorrectCourierId() {

        int incorrectCourierId = -1;

        String actualResponseMessage = orderClient.acceptOrder(track, incorrectCourierId)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .path("message");

        String expectedResponseMessage = "Курьера с таким id не существует";

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

    @Test
    public void checkErrorWhenTryingToAcceptOrderWithIncorrectTrack() {

        int incorrectTrack = 0;

        String actualResponseMessage = orderClient.acceptOrder(incorrectTrack, courierId)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .path("message");

        String expectedResponseMessage = "Заказа с таким id не существует";

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

}