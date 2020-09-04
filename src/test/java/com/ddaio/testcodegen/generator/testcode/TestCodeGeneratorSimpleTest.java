package com.ddaio.testcodegen.generator.testcode;

import com.ddaio.testcodegen.generator.Generator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestCodeGeneratorSimpleTest {

    private final Generator<String> defaultPasswordGenerator = l -> "password";
    private final TestCodeGenerator generator = new SimpleTestCodeGenerator(defaultPasswordGenerator);
    private final TestCodeGenerationRequest request = new TestCodeGenerationRequest()
            .setAllocatedTo("Java")
            .setLoginBase("SOUT")
            .setLoginStartingNumber(10)
            .setPasswordLength(6);

    @Test
    @DisplayName("Generates 0 test codes")
    void generate0TestCodes() {
        request.setQuantity(0);

        TestCodeGenerationResult result = generator.generateTestCodes(request);

        assertEquals(0, result.getQuantity());
    }

    @Test
    @DisplayName("Generates 10 test codes")
    void generates10TestCodes() {
        request.setQuantity(10);

        TestCodeGenerationResult result = generator.generateTestCodes(request);

        assertEquals(10, result.getQuantity());
        assertEquals(10, result.getTestCodes().size());
    }

    @Test
    @DisplayName("Generates test codes starting with MIM")
    void generatestestCodesStartingWithMIM() {
        request.setLoginBase("MIM")
                .setQuantity(3);

        TestCodeGenerationResult result = generator.generateTestCodes(request);

        assertAllTestCodesStartWith("MIM", result.getTestCodes(), 3);
    }

    @Test
    @DisplayName("Generates 0 test codes when the quantity is negative")
    void generates0CodesWhenQuantityIsNegative() {
        request.setQuantity(-2);

        TestCodeGenerationResult result = generator.generateTestCodes(request);

        assertEquals(0, result.getTestCodes().size());
        assertEquals(-2, result.getQuantity());
    }

    @Test
    @DisplayName("Generates 2 test codes with two different passwords")
    void generates2CodesWithDifferentPasswords() {
        TestCodeGenerator generator = new SimpleTestCodeGenerator(RandomStringUtils::randomAlphanumeric);
        request.setQuantity(2);

        TestCodeGenerationResult result = generator.generateTestCodes(request);

        List<TestCode> testCodes = result.getTestCodes();
        assertNotEquals(testCodes.get(1).getPassword(), testCodes.get(0).getPassword());
    }

    private void assertAllTestCodesStartWith(String startingBase, List<TestCode> testCodes, int quantity) {
        for (int i = 0; i < quantity; i++) {
            assertTrue(testCodes.get(i).getLogin().startsWith(startingBase));
        }
    }

}