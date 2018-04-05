package ru.mashnatash.learning_castle.data;

import com.google.gson.JsonObject;
import ru.mashnatash.learning_castle.data.userData.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class JDBCManager {
    private final Connection connection;

    public JDBCManager(Connection connection) {
        this.connection = connection;
    }


    /*
    PUBLIC SELECT-Ы К БД
     */
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

    public QuestionInfo[] getTestQuestions(int topic) {
        boolean isTest = true;
        ArrayList<QuestionInfo> questionInfos = new ArrayList<>();
        int quesNum = 1;
        while (isTest) {
            QuestionInfo question = new QuestionInfo();
            try (PreparedStatement randomizingStatement = connection.prepareStatement("select ques_id from questions where ques_no = ?")) {
                randomizingStatement.setInt(1,quesNum);
                ResultSet resultSet = randomizingStatement.executeQuery();
                ArrayList<Integer> ids = new ArrayList<>();
                while (resultSet.next()) {
                    ids.add(resultSet.getInt(1));
                }
                int randomRange = ids.size();
                if(randomRange > 0) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement("select ques_type, description from questions where topic = ? " +
                            "and ques_id = ?")) {
                        final Random random = new Random();
                        int id = random.nextInt(randomRange);
                        preparedStatement.setInt(1, topic);
                        preparedStatement.setInt(2, ids.get(id));
                        ResultSet result = preparedStatement.executeQuery();
                        if (result.next()) {
                            question.setId(ids.get(id));
                            question.setType(result.getInt(1));
                            String[] devidedQues = JSONManager.devideQuestion(result.getString(2));
                            question.setQuestion(devidedQues[0]);
                            for (int i = 1; i < devidedQues.length; i++) {
                                question.setAnswer(devidedQues[i]);
                            }
                        }
                        questionInfos.add(question);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    quesNum++;
                } else {
                    isTest = false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return questionInfos.toArray(new QuestionInfo[0]);
    }
    /*
    PUBLIC INSERT-Ы К БД
     */
    public void setAnswers(PlayerAnswers playerAnswers) {
        int topic = this.findTestTopic(playerAnswers.idOrder[0]);
        int test_id;
        if(topic > 0) {
            test_id = this.insertNewTest(topic);
            if (test_id >= 0) {
                for(int i = 0; i < playerAnswers.answers.length; i++) {
                    try (final PreparedStatement statement = this.connection.prepareStatement("insert into solutions (question,testnum,student,solution)" +
                            " values(?,?,?,?)")) {
                        statement.setInt(1, playerAnswers.idOrder[i]);
                        statement.setInt(2, test_id);
                        statement.setInt(3, playerAnswers.getUserId());
                        statement.setString(4,playerAnswers.answers[i]);
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            this.checkIsComplete(topic, playerAnswers.getUserId());
        }
    }


    /*
    * PRIVATES*/
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

    private int findTestTopic(int id) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("select topic from questions where ques_id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int insertNewTest(int topic) {
        try (final PreparedStatement statement = this.connection.prepareStatement("insert into tests (topic,testdate) values(?,'2018-03-12')", Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1,topic);
            //TODO: брать текущую дату
            statement.executeUpdate();
            try(ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void checkIsComplete(int topic, int userid) {
        try (final PreparedStatement statement = this.connection.prepareStatement("update results set iscomplete = 't'" +
                "WHERE stuent = ? and topic = ? and gamemark is not null")) {
            statement.setInt(1,userid);
            statement.setInt(2,topic);
            int upd = statement.executeUpdate();
            if(upd == 0) {

                try (final PreparedStatement statementInsert = this.connection.prepareStatement("insert into results (stuent,topic,iscomplete)" +
                        "values (?,?,'f')")) {
                    statementInsert.setInt(1, userid);
                    statementInsert.setInt(2, topic);
                    statementInsert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
