package com.ddaio.testcodegen.generator.testcode.random;

import com.ddaio.testcodegen.generator.Generator;
import com.ddaio.testcodegen.generator.testcode.TestCode;
import com.ddaio.testcodegen.generator.testcode.TestCodeGenerationRequest;
import com.ddaio.testcodegen.generator.testcode.TestCodeGenerationResult;
import com.ddaio.testcodegen.generator.testcode.TestCodeGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RandomLoginTestCodeGeneratorTest {

    private final Generator<String> defaultLoginGenerator = l -> "login";
    private final TestCodeGenerator generator = new RandomLoginTestCodeGenerator(defaultLoginGenerator);
    private final TestCodeGenerationRequest request = new TestCodeGenerationRequest()
            .setAllocatedTo("Java")
            .setLoginBase("SOUT")
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
    @DisplayName("Generates 0 test codes when the quantity is negative")
    void generates0CodesWhenQuantityIsNegative() {
        request.setQuantity(-2);

        TestCodeGenerationResult result = generator.generateTestCodes(request);

        assertEquals(0, result.getTestCodes().size());
        assertEquals(-2, result.getQuantity());
    }

    @Test
    @DisplayName("Generates 2 test codes with two different logins")
    void generates2CodesWithDifferentLogins() {
        TestCodeGenerator generator = new RandomLoginTestCodeGenerator(RandomStringUtils::randomAlphanumeric);
        request.setQuantity(2);

        TestCodeGenerationResult result = generator.generateTestCodes(request);

        List<TestCode> testCodes = result.getTestCodes();
        assertNotEquals(testCodes.get(1).getLogin(), testCodes.get(0).getLogin());
    }

    @Test
    @DisplayName("Generates 2 test codes with the same password")
    void generates2CodesWithEqualPassword() {
        request.setQuantity(2)
                .setLoginBase("KSrw15");

        TestCodeGenerationResult result = generator.generateTestCodes(request);

        List<TestCode> testCodes = result.getTestCodes();
        assertEquals(testCodes.get(0).getPassword(), testCodes.get(1).getPassword());
        assertEquals( "KSrw15", testCodes.get(0).getPassword());
    }


    @Test
    @DisplayName("Generates test codes with login length of size 6")
    void generatesCodesWithLoginLengthEqual6() {
        TestCodeGenerator generator = new RandomLoginTestCodeGenerator(RandomStringUtils::randomAlphanumeric);
        request.setQuantity(2)
                .setPasswordLength(6)
                .setLoginBase("KSrw15");

        TestCodeGenerationResult result = generator.generateTestCodes(request);

        List<TestCode> testCodes = result.getTestCodes();
        assertEquals(6, testCodes.get(0).getLogin().length());
    }

    private void assertAllTestCodesStartWith(String startingBase, List<TestCode> testCodes, int quantity) {
        for (int i = 0; i < quantity; i++) {
            assertTrue(testCodes.get(i).getLogin().startsWith(startingBase));
        }
    }

}