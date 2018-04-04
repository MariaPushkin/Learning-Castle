package ru.mashnatash.learning_castle.data.userData;

import java.util.ArrayList;

public class Student {
    private String name;
    private ArrayList<Topic> topics;

    public Student() {
        topics = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addTopic(int testmark, int gamemark) {
        topics.add(new Topic(testmark,gamemark));
    }

    class Topic {
        private int testmark;
        private int gamemark;

        public Topic(int test, int game) {
            this.gamemark = game;
            this.testmark = test;
        }
    }
}

