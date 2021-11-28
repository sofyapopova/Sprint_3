import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@DisplayName("Параметризованные тесты на создание курьера без указания обязательных полей")
@RunWith(Parameterized.class)
public class CourierCreationParameterizedTest {

    private final boolean setLogin;
    private final boolean setPassword;
    private final boolean setFirstName;

    private CourierClient courierClient;
    private Courier courier;

    public CourierCreationParameterizedTest(boolean setLogin, boolean setPassword, boolean setFirstName) {
        this.setLogin = setLogin;
        this.setPassword = setPassword;
        this.setFirstName = setFirstName;
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new CourierBuilder().random(setLogin, setPassword, setFirstName).buildCourier();
    }

    @Parameterized.Parameters
    public static Object[] getData() {
        return new Object[][]{
                {false, false, false},
                {false, false, true},
                {false, true, false},
                {false, true, true},
                {true, false, false},
                {true, false, true},
                {true, true, false}
        };
    }

    @Test
    public void checkCourierCannotBeCreatedWithoutRequiredParameters() {

        String actualResponseMessage = courierClient.createCourier(courier)
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        String expectedResponseMessage = "Недостаточно данных для создания учетной записи";

        assertEquals("Response message is incorrect", expectedResponseMessage, actualResponseMessage);
    }

}
