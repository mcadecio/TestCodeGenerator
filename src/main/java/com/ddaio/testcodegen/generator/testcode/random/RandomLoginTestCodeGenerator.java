package com.ddaio.testcodegen.generator.testcode.random;

import com.ddaio.testcodegen.generator.Generator;
import com.ddaio.testcodegen.generator.testcode.TestCode;
import com.ddaio.testcodegen.generator.testcode.TestCodeGenerationRequest;
import com.ddaio.testcodegen.generator.testcode.TestCodeGenerationResult;
import com.ddaio.testcodegen.generator.testcode.TestCodeGenerator;
import com.google.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomLoginTestCodeGenerator implements TestCodeGenerator {

    private final Generator<String> loginGenerator;

    @Inject
    public RandomLoginTestCodeGenerator(Generator<String> loginGenerator) {
        this.loginGenerator = loginGenerator;
    }

    @Override
    public TestCodeGenerationResult generateTestCodes(TestCodeGenerationRequest request) {
        int startingNumber = request.getLoginStartingNumber();
        int quantity = request.getQuantity();
        List<TestCode> testCodes = IntStream.range(startingNumber, startingNumber + quantity)
                .mapToObj(i -> createNewTestCode(request))
                .collect(Collectors.toList());
        return new TestCodeGenerationResult(testCodes, quantity);

    }

    private TestCode createNewTestCode(TestCodeGenerationRequest request) {
        String login = generateRandomLogin(request.getPasswordLength());
        String password = request.getLoginBase();
        return new TestCode(request.getAllocatedTo(), login, password);
    }

    private String generateRandomLogin(int loginLength) {
        return loginGenerator.generate(loginLength);
    }
}
