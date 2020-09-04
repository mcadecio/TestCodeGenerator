package com.ddaio.testcodegen.generator.testcode;

public class TestCodeGenerationRequest {

    private String allocatedTo;
    private String loginBase;
    private int loginStartingNumber;
    private int quantity;
    private int passwordLength;

    public TestCodeGenerationRequest() {
    }

    public String getAllocatedTo() {
        return allocatedTo;
    }

    public String getLoginBase() {
        return loginBase;
    }

    public int getLoginStartingNumber() {
        return loginStartingNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public TestCodeGenerationRequest setAllocatedTo(String allocatedTo) {
        this.allocatedTo = allocatedTo;
        return this;
    }

    public TestCodeGenerationRequest setLoginBase(String loginBase) {
        this.loginBase = loginBase;
        return this;
    }

    public TestCodeGenerationRequest setLoginStartingNumber(int loginStartingNumber) {
        this.loginStartingNumber = loginStartingNumber;
        return this;
    }

    public TestCodeGenerationRequest setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public TestCodeGenerationRequest setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        return this;
    }
}
