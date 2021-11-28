import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@DisplayName("ѕараметризованные тесты на авторизацию курьера без указани€ об€зательных полей")
@RunWith(Parameterized.class)
public class LoginCourierParameterizedTest {

    private final String login;
    private final String password;

    private CourierClient courierClient;
    private static final Courier courier = new CourierBuilder().random().buildCourier();

    public LoginCourierParameterizedTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courierClient.createCourier(courier);
    }

    @After
    public void tearDown() {
        int courierId = courierClient.loginAndGetCourierId(CourierCredentials.from(courier));
        courierClient.deleteCourier(courierId);
    }

    @Parameterized.Parameters
    public static Object[] getData() {
        return new Object[][]{
                {courier.getLogin(), null},
                {null, courier.getPassword()},
                {null, null}
        };
    }

    @Test
    public void checkCourierCannotBeLoggedInWithoutRequiredParameters() {

        String actualResponseMessage = courierClient.loginCourier(new CourierCredentials(login, password))
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        String expectedResponseMessage = "Ќедостаточно данных дл€ входа";

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

}
