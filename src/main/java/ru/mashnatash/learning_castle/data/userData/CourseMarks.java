package ru.mashnatash.learning_castle.data.userData;

import java.util.ArrayList;
import java.util.Arrays;

public class CourseMarks {
    int code;
    private int course_id;
    private int number_of_topics;
    private ArrayList<Student> students;

    public CourseMarks() {
        students = new ArrayList<>();
        code = 2;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public void setNumber_of_topics(int number_of_topics) {
        this.number_of_topics = number_of_topics;
    }

    public void addStudent(Student student) {
        students.add(student);
    }
}
