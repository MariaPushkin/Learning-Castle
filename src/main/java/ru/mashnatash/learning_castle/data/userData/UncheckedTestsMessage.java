package ru.mashnatash.learning_castle.data.userData;

import java.util.Arrays;

public class UncheckedTestsMessage {
    String name;
    UncheckedTest[] uncheckedTests;

    public UncheckedTestsMessage(String name, UncheckedTest[] uncheckedTests) {
        this.name = name;
        this.uncheckedTests = Arrays.copyOf(uncheckedTests, uncheckedTests.length);
    }
}
