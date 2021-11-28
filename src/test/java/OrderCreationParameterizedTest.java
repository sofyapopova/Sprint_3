import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@DisplayName("ѕараметризованные тесты на создание заказа")
@RunWith(Parameterized.class)
public class OrderCreationParameterizedTest {

    private final String[] color;

    private int track;
    private OrderClient orderClient;
    private Order order;

    public OrderCreationParameterizedTest(String[] color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        order = Order.getRandom().setColor(color);
    }

    @After
    public void orderCancel() {
        orderClient.cancelOrder(track)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Parameterized.Parameters
    public static Object[] getData() {
        return new Object[][]{
                {new String[]{"GREY", "BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK"}},
                {new String[]{}}
        };
    }

    @Test
    public void checkOrderCanBeCreatedWithDifferentColorValues() {

        track = orderClient.createOrder(order)
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("track");

        assertThat("Order track is incorrect", track, is(notNullValue()));
    }

}
