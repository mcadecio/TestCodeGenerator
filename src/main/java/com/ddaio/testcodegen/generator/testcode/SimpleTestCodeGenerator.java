package com.ddaio.testcodegen.generator.testcode;

import com.ddaio.testcodegen.generator.Generator;
import com.google.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleTestCodeGenerator implements TestCodeGenerator {

    private final Generator<String> passwordGenerator;

    @Inject
    public SimpleTestCodeGenerator(Generator<String> passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    @Override
    public TestCodeGenerationResult generateTestCodes(TestCodeGenerationRequest request) {
        int startingNumber = request.getLoginStartingNumber();
        int quantity = request.getQuantity();
        List<TestCode> testCodes = IntStream.range(startingNumber, startingNumber + quantity)
                .mapToObj(i -> createNewTestCode(request, i))
                .collect(Collectors.toList());
        return new TestCodeGenerationResult(testCodes, quantity);

    }

    private TestCode createNewTestCode(TestCodeGenerationRequest request, int number) {
        String login = request.getLoginBase() + number;
        String password = generateRandomPassword(request.getPasswordLength());
        return new TestCode(request.getAllocatedTo(), login, password);
    }

    private String generateRandomPassword(int passwordLength) {
        return passwordGenerator.generate(passwordLength);
    }
}
