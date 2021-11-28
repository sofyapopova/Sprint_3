import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DisplayName("Тесты на создание курьера")
public class CourierCreationTest {

    private CourierClient courierClient;
    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new CourierBuilder().random().buildCourier();
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }

    @Test
    public void checkCourierCanBeCreated() {

        boolean isCourierCreated = courierClient.createCourier(courier)
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");

        courierId = courierClient.loginAndGetCourierId(CourierCredentials.from(courier));

        assertTrue("Courier is not created", isCourierCreated);
        assertThat("Courier Id is incorrect", courierId, is(notNullValue()));
    }

    @Test
    public void checkErrorWhenCreatingTheSameCourier() {

        courierClient.createCourier(courier)
                .then()
                .assertThat()
                .statusCode(201);

        String actualResponseMessage = courierClient.createCourier(courier)
                .then()
                .assertThat()
                .statusCode(409)
                .extract()
                .path("message");

        courierId = courierClient.loginAndGetCourierId(CourierCredentials.from(courier));

        String expectedResponseMessage = "Этот логин уже используется. Попробуйте другой.";

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

    @Test
    public void checkErrorMessageWhenCreatingCourierWithExistingLogin() {

        Courier courierWithExistingLogin = new CourierBuilder()
                .random(false, true, true)
                .login(courier.getLogin())
                .buildCourier();

        courierClient.createCourier(courier)
                .then()
                .assertThat()
                .statusCode(201);

        String actualResponseMessage = courierClient.createCourier(courierWithExistingLogin)
                .then()
                .assertThat()
                .statusCode(409)
                .extract()
                .path("message");

        courierId = courierClient.loginAndGetCourierId(CourierCredentials.from(courier));

        String expectedResponseMessage = "Этот логин уже используется. Попробуйте другой.";

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

}
