package com.ddaio.testcodegen.generator.testcode;

import com.ddaio.testcodegen.generator.Generator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestCodeGeneratorTest {

    private final Generator<String> defaultPasswordGenerator = l -> "password";

    @Test
    @DisplayName("Generates 0 test codes")
    void generate0TestCodes() {
        Generator<TestCodeGenerationResult> generator = new TestCodeGenerator(
                "SOUT",
                10,
                6,
                defaultPasswordGenerator
        );

        TestCodeGenerationResult result = generator.generate(0);

        assertEquals(0, result.getQuantity());
    }

    @Test
    @DisplayName("Generates 10 test codes")
    void generates10TestCodes() {
        Generator<TestCodeGenerationResult> generator = new TestCodeGenerator(
                "ABC",
                10,
                6,
                defaultPasswordGenerator
        );

        TestCodeGenerationResult result = generator.generate(10);

        assertEquals(10, result.getQuantity());
        assertEquals(10, result.getTestCodes().size());
    }

    @Test
    @DisplayName("Generates test codes starting with MIM")
    void generatestestCodesStartingWithMIM() {
        Generator<TestCodeGenerationResult> generator = new TestCodeGenerator(
                "MIM",
                10,
                6,
                defaultPasswordGenerator
        );

        TestCodeGenerationResult result = generator.generate(3);

        assertAllTestCodesStartWith("MIM", result.getTestCodes(), 3);
    }

    @Test
    @DisplayName("Generates 0 test codes when the quantity is negative")
    void generates0CodesWhenQuantityIsNegative() {
        Generator<TestCodeGenerationResult> generator = new TestCodeGenerator(
                "MIM",
                10,
                6,
                defaultPasswordGenerator
        );

        TestCodeGenerationResult result = generator.generate(-2);

        assertEquals(0, result.getTestCodes().size());
        assertEquals(-2, result.getQuantity());
    }

    @Test
    @DisplayName("Generates 2 test codes with two different passwords")
    void generates2CodesWithDifferentPasswords() {

    }

    private void assertAllTestCodesStartWith(String startingBase, List<TestCode> testCodes, int quantity) {
        for (int i = 0; i < quantity; i++) {
            assertTrue(testCodes.get(i).getUsername().startsWith(startingBase));
        }
    }

}