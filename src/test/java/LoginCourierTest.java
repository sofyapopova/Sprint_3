import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

@DisplayName("Тесты на авторизацию курьера")
public class LoginCourierTest {

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
        courierId = courierClient.loginAndGetCourierId(CourierCredentials.from(courier));
        courierClient.deleteCourier(courierId);
    }

    @Test
    public void checkCourierCanLogin() {

        courierClient.createCourier(courier)
                .then()
                .assertThat()
                .statusCode(201);

        assertThat("Courier Id is incorrect", courierId, is(notNullValue()));
    }

    @Test
    public void checkErrorMessageWhenLoginWithIncorrectPassword() {

        courierClient.createCourier(courier);

        String login = courier.getLogin();
        String incorrectPassword = RandomStringUtils.randomAlphabetic(10);

        String actualResponseMessage = courierClient.loginCourier(new CourierCredentials(login, incorrectPassword))
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .path("message");

        String expectedResponseMessage = "Учетная запись не найдена";

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

    @Test
    public void checkErrorMessageWhenLoginWithIncorrectLogin() {

        courierClient.createCourier(courier);

        String incorrectLogin = RandomStringUtils.randomAlphabetic(10);
        String password = courier.getPassword();

        String actualResponseMessage = courierClient.loginCourier(new CourierCredentials(incorrectLogin, password))
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .path("message");

        String expectedResponseMessage = "Учетная запись не найдена";

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

    @Test
    public void checkErrorMessageWhenLoginAsNonExistingCourier() {

        String nonExistingLogin = RandomStringUtils.randomAlphabetic(10);
        String nonExistingPassword = RandomStringUtils.randomAlphabetic(10);

        String actualResponseMessage = courierClient.loginCourier(new CourierCredentials(nonExistingLogin, nonExistingPassword))
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .path("message");

        String expectedResponseMessage = "Учетная запись не найдена";

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

}

