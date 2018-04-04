package ru.mashnatash.learning_castle.data.userData;

import java.util.ArrayList;

public class TeacherCoursesInfo {
    String teacherName;
    ArrayList<Course> courses;

    class Course {
        int code = 1;
        int id;
        String name;
        String startYear;
        //String className;

        Course(int id, String name, String startYear) {
            this.id = id;
            this.name = name;
            this.startYear = startYear;
        }
    }

    public TeacherCoursesInfo() {
        courses = new ArrayList<>();
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void addCourse(int id, String name, String year) {
        this.courses.add(new Course(id, name, year));
    }

    public String gerString() {
        return this.teacherName + this.courses.get(0).id + this.courses.get(0).name + this.courses.get(0).startYear;
    }
}
