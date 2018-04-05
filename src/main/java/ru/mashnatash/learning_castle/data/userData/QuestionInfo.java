package ru.mashnatash.learning_castle.data.userData;

import java.util.ArrayList;
import java.util.Arrays;

public class QuestionInfo {
    private int type; // 1 - oneA, 2 - manyA, 3 - textA
    private int id;
    private String question;
    private ArrayList<String> answers;

    public QuestionInfo() { this.answers = new ArrayList<>();}

    public void setId(int id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answers.add(answer);
    }

    public String getQuestion() {
        return question;
    }

    public String getAll() {
        return type + " " + id + " " + question + " " + answers.get(0);
    }
}
