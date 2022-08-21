package com.ddaio.testcodegen.generator.testcode;

public class TestCode {
    private static final int DEFAULT_NUMBER_OF_ATTEMPTS = 1;

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

    public int getNAttempts() {
        return DEFAULT_NUMBER_OF_ATTEMPTS;
    }

    @Override
    public String toString() {
        return String.format(
                "%s,%s,%s,%s",
                allocateTo,
                "",
                login,
                password
        );
    }
}
