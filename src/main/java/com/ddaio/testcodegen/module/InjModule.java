package com.ddaio.testcodegen.module;

import com.ddaio.testcodegen.generator.Generator;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.commons.lang3.RandomStringUtils;

public class InjModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    public Generator<String> passwordGenerator() {
        return RandomStringUtils::randomAlphanumeric;
    }
}
