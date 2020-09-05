package com.ddaio.testcodegen.generator.testcode;

public class TestCode {

    private final String country = "";
    private final int nAttempts = 1;
    private final String allocateTo;
    private final String login;
    private final String password;

    public TestCode(String allocateTo, String login, String password) {
        this.allocateTo = allocateTo;
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }

    public int getNAttempts() {
        return nAttempts;
    }

    public String getAllocateTo() {
        return allocateTo;
    }

    @Override
    public String toString() {
        return String.format(
                "%s,%s,%s,%s",
                allocateTo,
                country,
                login,
                password
        );
    }
}
