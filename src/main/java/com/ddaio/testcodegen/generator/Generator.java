package com.ddaio.testcodegen.generator;

@FunctionalInterface
public interface Generator<T> {

    T generate(int n);
}
