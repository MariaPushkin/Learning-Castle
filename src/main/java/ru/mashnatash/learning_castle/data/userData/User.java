package ru.mashnatash.learning_castle.data.userData;

public class User {
    int status;
    int code;
    int id;
    String gender;
    boolean[] isCompleted;
    boolean[] completedTests;
    int[] minigameRecord;

    public User() {
        status = 1;
        code = 1;
        id = 1;
        gender = "f";
        isCompleted = new boolean[]{true, false, false};
        completedTests = new boolean[]{false, false, false};
        minigameRecord = new int[]{6,-100,-100};
    }
}
