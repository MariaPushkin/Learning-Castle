package ru.mashnatash.learning_castle.data.userData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import ru.mashnatash.learning_castle.data.JDBCManager;
import ru.mashnatash.learning_castle.data.JSONManager;

import java.sql.Connection;

public class UserActions {

    public static String authorization(Connection connection, boolean isGame, JsonObject userData) {
        final JDBCManager manager = new JDBCManager(connection);
        if (isGame) {
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();
                return gson.toJson(manager.gameAuthorize(userData));
        } else {
            return JSONManager.toJsonString(manager.webAuthorize(userData));
        }
    }

    public static void testCompletion(String answerData) {
        //TODO: Добавить коннекшн к бд
        Gson gson = new Gson();
        PlayerAnswers playerAnswers = new PlayerAnswers();
        playerAnswers = gson.fromJson(answerData, PlayerAnswers.class);
    }

    public static String getTeacherCourses(Connection connection, JsonObject userData) {
        final JDBCManager manager = new JDBCManager(connection);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(manager.getCourses(userData));
    }

    public static String getCourseResultTable(Connection connection, int courseId) {
        final JDBCManager manager = new JDBCManager(connection);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(manager.getMarks(courseId));
    }

    public static String getTest(Connection connection, int topic) {
        final JDBCManager manager = new JDBCManager(connection);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(manager.getTestQuestions(topic));
    }
}
