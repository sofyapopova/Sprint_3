import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@DisplayName("Тесты на получение списка заказов")
public class OrderListTest {

    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    public void checkReturnedOrderListIsNotEmpty() {

        int orderListSize = orderClient.getOrderList()
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("orders.size()");

        assertThat(orderListSize, greaterThan(0));
    }

}
