package ru.mashnatash.learning_castle.data.userData;

import java.util.ArrayList;
import java.util.Arrays;

public class UncheckedTest {
    private int topic;
    private String topic_name;
    private int student_id;
    private int test_num;
    private String student_name;
    private ArrayList<Question> questions;

    public UncheckedTest() {
        questions = new ArrayList<>();
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public void setStudent_id(int student_id) {this.student_id = student_id;}

    public void setTest_num(int test_num) {this.test_num = test_num;}

    public String getStudent_name() {
        return student_name;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public void setQuestions(String question, String answer, int type, int score, String[] answers) {
        questions.add(new Question(question, answer, type, score, answers));
    }


    class Question {
       String question;
       String[] answers;
       String student_answer;
       int type;
       int score;

       public Question(String question, String answer, int type, int score, String[] answers) {
           this.question = question;
           this.student_answer = answer;
           this.type = type;
           this.score = score;
           this.answers = Arrays.copyOfRange(answers, 1,answers.length);
           //System.out.println(answers);
           //System.out.println(this.answers);
       }

    }
}
