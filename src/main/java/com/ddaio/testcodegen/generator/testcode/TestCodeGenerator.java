package com.ddaio.testcodegen.generator.testcode;

import com.ddaio.testcodegen.generator.Generator;
import com.google.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestCodeGenerator implements Generator<TestCodeGenerationResult> {

    private final String base;
    private final int passwordLength;
    private final int startingNumber;
    private final Generator<String> passwordGenerator;

    public TestCodeGenerator(
            String base,
            int startingNumber,
            int passwordLength,
            Generator<String> passwordGenerator) {
        this.base = base;
        this.startingNumber = startingNumber;
        this.passwordLength = passwordLength;
        this.passwordGenerator = passwordGenerator;
    }

    @Override
    public TestCodeGenerationResult generate(int quantity) {
         List<TestCode> testCodes = IntStream.range(startingNumber, startingNumber + quantity)
                .mapToObj(i -> createNewTestCode(base, i, passwordLength))
                .collect(Collectors.toList());
        return new TestCodeGenerationResult(testCodes, quantity);

    }

    private TestCode createNewTestCode(String base, int startingNumber, int passwordLength) {
        String username = base + startingNumber;
        String password = generateRandomPassword(passwordLength);
        return new TestCode(username, password);
    }

    private String generateRandomPassword(int passwordLength) {
        return passwordGenerator.generate(passwordLength);
    }
}
