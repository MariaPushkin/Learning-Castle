package ru.mashnatash.learning_castle.data;

import com.google.gson.JsonObject;
import ru.mashnatash.learning_castle.data.userData.*;
import ru.mashnatash.learning_castle.tools.JSONManager;
import ru.mashnatash.learning_castle.tools.MarkCounter;

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

    /**
     * Авторизация для юзера из приложения-игры
     *
     * @param userData - JsonObject с данными о логине и пароле из приложения
     * @return - обьект класса User с данными, необходимыми игре о данном пользователе
     */
    public User gameAuthorize(JsonObject userData) {
        User user = new User();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id, sex FROM users WHERE login=? AND u_password=? and usertype='s'")) {
            preparedStatement.setString(1, JSONManager.removeQuots(userData.get("login").toString()));
            preparedStatement.setString(2, JSONManager.removeQuots(userData.get("password").toString()));
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                user.setStatus(1);
                int userid = result.getInt(1);
                user.setId(userid);
                user.setGender(result.getString(2));
                int numOfTopics = this.getNumberOfTopics();
                if (numOfTopics > 0) {
                    for (int i = 1; i <= numOfTopics; i++) {
                        user.addNewIsComplete(this.getIsComplete(i, userid));
                        user.addNewTestStatus(this.getCompletedTests(i, userid));
                        user.addNewGameRecord(this.getMinigameRecord(i, userid));
                    }
                }
            } else {
                user.setStatus(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Map<String, String> webAuthorize(JsonObject userData) {
        Map<String, String> answerMap = new HashMap<String, String>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id, userType FROM users WHERE login=? AND u_password=?")) {
            //Устанавливаем в нужную позицию значения определённого типа
            System.out.println(userData.get("login").toString());
            preparedStatement.setString(1, JSONManager.removeQuots(userData.get("login").toString()));
            preparedStatement.setString(2, JSONManager.removeQuots(userData.get("password").toString()));
            //выполняем запрос
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                answerMap.put("status", "OK");
                answerMap.put("id", result.getString("user_id"));
                System.out.println(result.getString("user_id"));
                System.out.println(result.getString("userType"));
                switch (result.getString("userType")) {
                    case "t":
                        answerMap.put("rights", "teacher");
                        break;
                    case "s":
                        answerMap.put("rights", "student");
                        break;
                    case "a":
                        answerMap.put("rights", "administrator");
                        break;
                    default:
                        break;
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
     * @param userData - данные по учителю, полученные от клиента с сайта
     * @return класс с данными по всем курсам преподавателя
     */
    public TeacherCoursesInfo getCourses(JsonObject userData) {
        TeacherCoursesInfo courseInfo = new TeacherCoursesInfo();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select us.fullname, co.course_name, co.start_year, co.course_id\n" +
                "        from users as us, courses as co\n" +
                "        where us.user_id = ? and us.user_id = co.teacher")) {
            preparedStatement.setInt(1, userData.get("id").getAsInt());
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                courseInfo.setTeacherName(result.getString(1));
                courseInfo.addCourse(result.getInt(4), result.getString(2), result.getString(3));
                while (result.next()) {
                    courseInfo.addCourse(result.getInt(4), result.getString(2), result.getString(3));
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
        for (int student : studentlist) {
            String studentName = null;
            Student studentUser = new Student();
            for (int topic : topicsList) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("select us.fullname, res.testmark, res.gamemark from results as res, users as us \n" +
                        "where res.stuent = ? and res.stuent = us.user_id and res.topic = ?;")) {
                    preparedStatement.setInt(1, student);
                    preparedStatement.setInt(2, topic);
                    ResultSet result = preparedStatement.executeQuery();
                    if (result.next()) {
                        if (studentName == null) {
                            studentName = result.getString(1);
                            studentUser.setName(studentName);
                        }
                        studentUser.addTopic(result.getInt(2), result.getInt(3));
                    } else {
                        studentUser.addTopic(0, 0);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (studentUser.getName() == null) {
                try (PreparedStatement preparedStatement2 = connection.prepareStatement("select fullname from users \n" +
                        "where user_id = ?;")) {
                    System.out.println(student);
                    preparedStatement2.setInt(1, student);
                    ResultSet result2 = preparedStatement2.executeQuery();
                    if (result2.next()) {
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
                randomizingStatement.setInt(1, quesNum);
                ResultSet resultSet = randomizingStatement.executeQuery();
                ArrayList<Integer> ids = new ArrayList<>();
                while (resultSet.next()) {
                    ids.add(resultSet.getInt(1));
                }
                int randomRange = ids.size();
                if (randomRange > 0) {
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

    public UncheckedTest[] getUncheckedTests(int teacherId) {
        ArrayList<UncheckedTest> listOfTests = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select tes.test_num, tes.topic, top.topic_name from tests as tes,topics as top\n" +
                "where tes.ischecked = 'f' and tes.topic = top.topic_id")) {
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                int testnum = result.getInt(1);
                int topic = result.getInt(2);
                String topicName = result.getString(3);
                try (PreparedStatement questionsStatement = connection.prepareStatement("select distinct sol.sol_num, sol.student, sol.solution, sol.question from solutions as sol, users as use, courses as cou\n" +
                        "where sol.student = use.user_id and use.class = cou.class_no and cou.teacher = ? and sol.testnum = ?")) {
                    questionsStatement.setInt(1, teacherId);
                    questionsStatement.setInt(2, testnum);
                    ResultSet questionResult = questionsStatement.executeQuery();
                    UncheckedTest uncheckedTest = new UncheckedTest();
                    uncheckedTest.setTopic(topic);
                    uncheckedTest.setTopic_name(topicName);
                    while (questionResult.next()) {
                        uncheckedTest.setStudent_name(this.getNameById(questionResult.getInt(2)));
                        String questionDescription;
                        int questionType;
                        String answer = questionResult.getString(3);
                        int score;
                        try (PreparedStatement qaStatement = connection.prepareStatement("select description, ques_type\n" +
                                "from questions where ques_id = ?")) {
                           qaStatement.setInt(1,questionResult.getInt(4));
                           ResultSet qaResult = qaStatement.executeQuery();
                           if(qaResult.next()) {
                               questionDescription = qaResult.getString(1);
                               questionType = qaResult.getInt(2);
                               score = MarkCounter.countMark(questionType, questionDescription, answer);
                               uncheckedTest.setQuestions(questionDescription, answer, questionType, score);
                           }
                        }
                    }
                    if(uncheckedTest.getStudent_name() != null) {
                        listOfTests.add(uncheckedTest);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfTests.toArray(new UncheckedTest[0]);
    }

    public String getNameById(int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("select fullname from users where user_id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return result.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    PUBLIC INSERT-Ы К БД
     */
    public void setAnswers(PlayerAnswers playerAnswers) {
        int topic = this.findTestTopic(playerAnswers.idOrder[0]);
        int test_id;
        if (topic > 0) {
            test_id = this.insertNewTest(topic);
            if (test_id >= 0) {
                for (int i = 0; i < playerAnswers.answers.length; i++) {
                    try (final PreparedStatement statement = this.connection.prepareStatement("insert into solutions (question,testnum,student,solution)" +
                            " values(?,?,?,?)")) {
                        statement.setInt(1, playerAnswers.idOrder[i]);
                        statement.setInt(2, test_id);
                        statement.setInt(3, playerAnswers.getUserId());
                        statement.setString(4, playerAnswers.answers[i]);
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            this.isCompleteTest(topic, playerAnswers.getUserId());
        }
    }

    public void setGameRecord(JsonObject userData) {
        int student = userData.get("student").getAsInt();
        int topic = userData.get("topic").getAsInt();
        int score = userData.get("score").getAsInt();
        int maxscore = userData.get("maxscore").getAsInt();
        if (!this.checkIfGamePlayedandUpdate(topic, student, score)) {
            try (final PreparedStatement statement = this.connection.prepareStatement("insert into games (game_name,topic,student,score,maxscor)" +
                    " values('-',?,?,?,?)")) {
                statement.setInt(1, topic);
                statement.setInt(2, student);
                statement.setInt(3, score);
                statement.setInt(4, maxscore);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.isCompleteGame(topic, student, MarkCounter.countMark(score, maxscore, topic));
    }

    /*
     * PRIVATES*/
    private ArrayList<Integer> getListOfCourseStudent(int courseId) {
        ArrayList<Integer> studentlist = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select us.user_id from courses as co, users as us\n" +
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
        try (PreparedStatement preparedStatement = connection.prepareStatement("select top.topic_id from courses as co, topics as top \n" +
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

    private int getNumberOfTopics() {
        try (PreparedStatement preparedStatement = connection.prepareStatement("select count(*) from topics")) {
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int findTestTopic(int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("select topic from questions where ques_id = ?")) {
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
        try (final PreparedStatement statement = this.connection.prepareStatement("insert into tests (topic,testdate,ischecked) values(?,'2018-03-12','f')", Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, topic);
            //TODO: брать текущую дату
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void isCompleteGame(int topic, int userid, int mark) {
        try (final PreparedStatement statement = this.connection.prepareStatement("update results set iscomplete = 't'" +
                "WHERE stuent = ? and topic = ? and gamemark is null")) {
            statement.setInt(1, userid);
            statement.setInt(2, topic);
            statement.executeUpdate();
            try (final PreparedStatement updMarkStatement = this.connection.prepareStatement("update results set gamemark = ?" +
                    "WHERE stuent = ? and topic = ?")) {
                updMarkStatement.setInt(1, mark);
                updMarkStatement.setInt(2, userid);
                updMarkStatement.setInt(3, topic);
                int upd = updMarkStatement.executeUpdate();
                if (upd == 0) {
                    try (final PreparedStatement statementInsert = this.connection.prepareStatement("insert into results (stuent,topic,gamemark,iscomplete)" +
                            "values (?,?,?,'f')")) {
                        statementInsert.setInt(1, userid);
                        statementInsert.setInt(2, topic);
                        statementInsert.setInt(3, mark);
                        statementInsert.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void isCompleteTest(int topic, int userid) {
        try (final PreparedStatement statement = this.connection.prepareStatement("update results set iscomplete = 't'" +
                "WHERE stuent = ? and topic = ? and gamemark is not null")) {
            statement.setInt(1, userid);
            statement.setInt(2, topic);
            int upd = statement.executeUpdate();
            if (upd == 0) {
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

    private boolean getIsComplete(int topic, int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("select iscomplete from results where stuent = ? and topic = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, topic);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return result.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean getCompletedTests(int topic, int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("select count(*) from results\n" +
                "where (testmark is not null or (testmark is null and iscomplete = 't') or\n" +
                "(testmark is null and gamemark is null)) and stuent = ? and topic = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, topic);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                if (result.getInt(1) == 1) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getMinigameRecord(int topic, int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("select score from games where topic = ? and student = ?")) {
            preparedStatement.setInt(1, topic);
            preparedStatement.setInt(2, id);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -100;
    }

    private boolean checkIfGamePlayedandUpdate(int topic, int id, int score) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("update games set score = ? where topic = ? and student = ?")) {
            preparedStatement.setInt(1, score);
            preparedStatement.setInt(2, topic);
            preparedStatement.setInt(3, id);
            int upd = preparedStatement.executeUpdate();
            if (upd > 0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
