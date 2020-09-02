package com.ddaio.testcodegen.generator.testcode;

public class TestCode {
    private final String username;
    private final String password;

    public TestCode(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
