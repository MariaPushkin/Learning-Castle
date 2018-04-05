package ru.mashnatash.learning_castle.data;

import com.google.gson.JsonObject;
import ru.mashnatash.learning_castle.data.userData.CourseMarks;
import ru.mashnatash.learning_castle.data.userData.TeacherCoursesInfo;
import ru.mashnatash.learning_castle.data.userData.User;
import ru.mashnatash.learning_castle.data.userData.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JDBCManager {
    private final Connection connection;

    public JDBCManager(Connection connection) {
        this.connection = connection;
    }

    public User gameAuthorize(JsonObject userData) {
        //TODO 2: Брать данные по авторизации для игры из бд
        User user = new User();
        return user;
    }

    public Map<String,String> webAuthorize(JsonObject userData) {
        System.out.println("Мы в авторизе");
        Map<String, String> answerMap = new HashMap<String, String>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id, userType FROM users WHERE login=? AND u_password=?")){
            //Устанавливаем в нужную позицию значения определённого типа
            System.out.println(userData.get("login").toString());
            preparedStatement.setString(1, JSONManager.removeQuots(userData.get("login").toString()));
            preparedStatement.setString(2, JSONManager.removeQuots(userData.get("password").toString()));
            System.out.println("Тут");
            //выполняем запрос
            ResultSet result = preparedStatement.executeQuery();
            if(result.next()) {
                answerMap.put("status","OK");
                answerMap.put("id",result.getString("user_id"));
                System.out.println(result.getString("user_id"));
                System.out.println(result.getString("userType"));
                switch (result.getString("userType")) {
                    case "t": answerMap.put("rights", "teacher");
                        break;
                    case "s": answerMap.put("rights", "student");
                        break;
                    case "a": answerMap.put("rights", "administrator");
                        break;
                    default: break;
                }
            } else {
                System.out.println("NeOK");
                answerMap.put("status", "NOT OK");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answerMap;
    }

    /**
     *
     * @param userData - данные по учителю, полученные от клиента с сайта
     * @return класс с данными по всем курсам преподавателя
     */
    public TeacherCoursesInfo getCourses(JsonObject userData) {
        TeacherCoursesInfo courseInfo = new TeacherCoursesInfo();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select us.fullname, co.course_name, co.start_year, co.course_id\n" +
                "        from users as us, courses as co\n" +
                "        where us.user_id = ? and us.user_id = co.teacher")) {
            preparedStatement.setInt(1, userData.get("id").getAsInt());
            ResultSet result = preparedStatement.executeQuery();
            if(result.next()) {
                courseInfo.setTeacherName(result.getString(1));
                courseInfo.addCourse(result.getInt(4) ,result.getString(2), result.getString(3));
                while (result.next()) {
                    courseInfo.addCourse(result.getInt(4) ,result.getString(2), result.getString(3));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseInfo;
    }

    public CourseMarks getMarks(int courseId) {
        CourseMarks marks = new CourseMarks();
        marks.setCourse_id(courseId);

        ArrayList<Integer> topicsList = new ArrayList<>();
        topicsList = this.getNumberOfTopics(courseId);
        marks.setNumber_of_topics(topicsList.size());

        //Количество студентов на курсе
        ArrayList<Integer> studentlist = new ArrayList<>();
        studentlist = this.getListOfCourseStudent(courseId);
        for (int student: studentlist) {
            String studentName = null;
            Student studentUser = new Student();
            for (int topic:topicsList) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("select us.fullname, res.testmark, res.gamemark from results as res, users as us \n" +
                        "where res.stuent = ? and res.stuent = us.user_id and res.topic = ?;")) {
                    preparedStatement.setInt(1, student);
                    preparedStatement.setInt(2, topic);
                    ResultSet result = preparedStatement.executeQuery();
                    if (result.next()) {
                        if(studentName == null) {
                            studentName = result.getString(1);
                            studentUser.setName(studentName);
                        }
                        studentUser.addTopic(result.getInt(2),result.getInt(3));
                    } else {
                        studentUser.addTopic(0,0);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(studentUser.getName() == null) {
                try (PreparedStatement preparedStatement2 = connection.prepareStatement("select fullname from users \n" +
                        "where user_id = ?;")) {
                    System.out.println(student);
                    preparedStatement2.setInt(1, student);
                    ResultSet result2 = preparedStatement2.executeQuery();
                    if(result2.next()) {
                        System.out.println("vnrgnv");
                        System.out.println(result2.getString(1));
                        studentUser.setName(result2.getString(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            marks.addStudent(studentUser);
        }
        return marks;
    }

    private ArrayList<Integer> getListOfCourseStudent(int courseId) {
        ArrayList<Integer> studentlist = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select us.user_id from courses as co, users as us\n" +
                "where co.course_id = ? and co.class_no = us.class order by us.fullname")) {
            preparedStatement.setInt(1, courseId);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                studentlist.add(result.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println(studentlist.get(0) + " " + studentlist.get(1));
        return studentlist;
    }

    private ArrayList<Integer> getNumberOfTopics(int courseId) {
        ArrayList<Integer> topicsList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select top.topic_id from courses as co, topics as top \n" +
                "               where co.course_id = ? and co.subject = top.subject")) {
            preparedStatement.setInt(1, courseId);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                topicsList.add(result.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topicsList;
    }
}
