import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {

    public final String firstName;
    public final String lastName;
    public final String address;
    public final String metroStation;
    public final String phone;
    public final int rentTime;
    public final String deliveryDate;
    public final String comment;
    public String[] color;

    public Order(
            String firstName,
            String lastName,
            String address,
            String metroStation,
            String phone,
            int rentTime,
            String deliveryDate,
            String comment,
            String[] color
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    public static Order getRandom() {

        return new Order(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomNumeric(1),
                RandomStringUtils.randomNumeric(11),
                Integer.parseInt(RandomStringUtils.randomNumeric(1)),
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                RandomStringUtils.randomAlphabetic(20),
                new String[]{}
        );
    }

    public Order setColor(String[] color) {
        this.color = color;
        return this;
    }
}
