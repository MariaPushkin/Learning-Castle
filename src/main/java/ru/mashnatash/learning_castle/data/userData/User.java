package ru.mashnatash.learning_castle.data.userData;

import java.util.ArrayList;

public class User {
    private int status; //1 - OK
    private int id;
    private String gender;
    private ArrayList<Boolean> isCompleted;
    private ArrayList<Boolean> completedTests;
    private ArrayList<Integer> minigameRecord; //-100 - в игру не играли

    public User() {
        status = 1;
        this.isCompleted = new ArrayList<>();
        this.completedTests = new ArrayList<>();
        this.minigameRecord = new ArrayList<>();
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void addNewIsComplete(boolean isComp) {
        this.isCompleted.add(isComp);
    }

    public void addNewTestStatus(boolean testSt) {
        this.completedTests.add(testSt);
    }

    public void addNewGameRecord(int rec) {
        this.minigameRecord.add(rec);
    }
}

