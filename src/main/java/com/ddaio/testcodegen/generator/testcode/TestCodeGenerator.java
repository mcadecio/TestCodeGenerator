package com.ddaio.testcodegen.generator.testcode;

@FunctionalInterface
public interface TestCodeGenerator {

    TestCodeGenerationResult generateTestCodes(TestCodeGenerationRequest request);
}
