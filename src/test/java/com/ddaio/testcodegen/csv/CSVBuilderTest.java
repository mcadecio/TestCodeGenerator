package com.ddaio.testcodegen.csv;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CSVBuilderTest {

    @Test
    @DisplayName("Creates comma-separated string containing" +
            " only the headers when the list of test code is empty")
    void createCommaSeparatedStringWithHeadersOnly() {
        CSVBuilder builder = new CSVBuilder();

        String actual = builder.createCSV(Collections.emptyList(), false);
        String expected = "Allocated to,Country,Login,Password\n";

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Creates comma-separated string containing number of attempts header")
    void createCommaSeparatedStringWithNumberOfAttemptsHeader() {
        CSVBuilder builder = new CSVBuilder();

        String actual = builder.createCSV(Collections.emptyList(), true);
        String expected = "Allocated to,Country,Login,Password,Number of Attempts\n";

        assertEquals(expected, actual);
    }



}