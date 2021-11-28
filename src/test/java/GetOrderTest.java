import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@DisplayName("Тесты на получение заказа")
public class GetOrderTest {

    private OrderClient orderClient;
    private int track;

    @Before
    public void setUp() {
        orderClient = new OrderClient();

        track = orderClient.createOrder(Order.getRandom())
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("track");
    }

    @After
    public void tearDown() {
        orderClient.cancelOrder(track)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void checkCorrectOrderIsReturnedByTrack() {

        int actualTrack = orderClient.getOrderByTrack(track)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("order.track");

        assertEquals("Got order with incorrect track", track, actualTrack);
    }

    @Test
    public void checkErrorWhenTryingToGetOrderWithIncorrectTrack() {

        int incorrectTrack = 0;

        String actualResponseMessage = orderClient.getOrderByTrack(incorrectTrack)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .path("message");

        String expectedResponseMessage = "Заказ не найден";

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

    @Test
    public void checkErrorWhenTryingToGetOrderWithoutTrack() {

        String actualResponseMessage = orderClient.getOrderByTrack(null)
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        String expectedResponseMessage = "Недостаточно данных для поиска";

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

}
