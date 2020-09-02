package com.ddaio.testcodegen.generator.testcode;

import java.util.List;

public class TestCodeGenerationResult {

    private final List<TestCode> testCodes;
    private final int quantity;

    public TestCodeGenerationResult(List<TestCode> testCodes, int quantity) {
        this.testCodes = testCodes;
        this.quantity = quantity;
    }

    public List<TestCode> getTestCodes() {
        return testCodes;
    }

    public int getQuantity() {
        return quantity;
    }
}
