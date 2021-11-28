import org.apache.commons.lang3.RandomStringUtils;

public class CourierBuilder {

    private String login;
    private String password;
    private String firstName;

    public Courier buildCourier() {
        return new Courier(login, password, firstName);
    }

    private String getRandomString() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public CourierBuilder random() {
        this.login = this.getRandomString();
        this.password = this.getRandomString();
        this.firstName = this.getRandomString();
        return this;
    }

    public CourierBuilder random(boolean setLogin, boolean setPassword, boolean setFirstName) {

        if (setLogin) {
            this.login = this.getRandomString();
        }

        if (setPassword) {
            this.password = this.getRandomString();
        }

        if (setFirstName) {
            this.firstName = this.getRandomString();
        }

        return this;
    }

    public CourierBuilder login(String login) {
        this.login = login;
        return this;
    }

}
