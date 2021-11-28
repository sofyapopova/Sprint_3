import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DisplayName("Тесты на удаление курьера")
public class CourierDeletionTest {

    private CourierClient courierClient;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    public void checkCourierCanBeDeleted() {

        Courier courier = new CourierBuilder().random().buildCourier();

        courierClient.createCourier(courier)
                .then()
                .assertThat()
                .statusCode(201);

        int courierId = courierClient.loginAndGetCourierId(CourierCredentials.from(courier));

        boolean isCourierDeleted = courierClient.deleteCourier(courierId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("ok");

        assertTrue("Courier has not been deleted", isCourierDeleted);
    }

    @Test
    public void checkErrorWhenDeleteCourierWithoutCourierId() {

        String actualResponseMessage = courierClient.deleteCourierWithoutCourierId()
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        String expectedResponseMessage = "Недостаточно данных для удаления курьера";

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

    @Test
    public void checkErrorWhenDeleteCourierWithNonExistingCourierId() {

        int nonExistingCourierId = -1;

        String actualResponseMessage = courierClient.deleteCourier(nonExistingCourierId)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .path("message");

        String expectedResponseMessage = "Курьера с таким id нет.";

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }
    
}
