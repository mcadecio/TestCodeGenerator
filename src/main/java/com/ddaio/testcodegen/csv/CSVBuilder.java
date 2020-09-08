package com.ddaio.testcodegen.csv;

import com.ddaio.testcodegen.generator.testcode.TestCode;

import java.util.List;
import java.util.stream.Collectors;

public class CSVBuilder {

    private static final String ALLOCATED_TO = "Allocated to";
    private static final String COUNTRY = "Country";
    private static final String LOGIN = "Login";
    private static final String PASSWORD = "Password";
    private static final String NUMBER_OF_ATTEMPTS = "Number of Attempts";

    public String createCSV(List<TestCode> testCodes, boolean shouldIncludeAttempts) {

        String headers = createHeaders(shouldIncludeAttempts);
        String content = createContent(testCodes, shouldIncludeAttempts);

        return headers + content;
    }

    private String createContent(List<TestCode> testCodes, boolean shouldIncludeAttempts) {
        return testCodes.stream()
                .map(testCode -> createTestCodeLine(shouldIncludeAttempts, testCode))
                .collect(Collectors.joining("\n"));
    }

    private String createTestCodeLine(boolean shouldIncludeAttempts, TestCode testCode) {
        String line;
        if (shouldIncludeAttempts) {
            line = testCode.toString() + "," + testCode.getNAttempts();
        } else {
            line = testCode.toString();
        }
        return line;
    }

    private String createHeaders(boolean shouldIncludeAttempts) {
        String headers = String.join(
                ",",
                ALLOCATED_TO,
                COUNTRY,
                LOGIN,
                PASSWORD
        );

        if (shouldIncludeAttempts) {
            headers = headers + "," + NUMBER_OF_ATTEMPTS;
        }

        headers += "\n";

        return headers;
    }
}
