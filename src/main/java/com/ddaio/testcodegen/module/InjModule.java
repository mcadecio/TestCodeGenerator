package com.ddaio.testcodegen.module;

import com.ddaio.testcodegen.generator.Generator;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import org.apache.commons.lang3.RandomStringUtils;

public class InjModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Named("passwordGenerator")
    public Generator<String> passwordGenerator() {
        return RandomStringUtils::randomAlphanumeric;
    }
}
