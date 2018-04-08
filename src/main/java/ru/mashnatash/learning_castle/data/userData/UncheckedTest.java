package ru.mashnatash.learning_castle.data.userData;

import java.util.ArrayList;

public class UncheckedTest {
    private int topic;
    private String topic_name;
    private String student_name;
    private ArrayList<Question> questions;

    public UncheckedTest() {
        questions = new ArrayList<>();
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public void setQuestions(String question, String answer, int type, int score) {
        questions.add(new Question(question, answer, type, score));
    }

    class Question {
       String questsion;
       String student_answer;
       int type;
       int score;

       public Question(String question, String answer, int type, int score) {
           this.questsion = question;
           this.student_answer = answer;
           this.type = type;
           this.score = score;
       }
    }
}
