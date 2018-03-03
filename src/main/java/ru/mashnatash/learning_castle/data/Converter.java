package ru.mashnatash.learning_castle.data;

public class Converter {
    public static String removeQuots(String str) {
        return str.substring(1, str.length() - 1);
    }
}
