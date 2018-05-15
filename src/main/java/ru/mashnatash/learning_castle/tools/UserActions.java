package ru.mashnatash.learning_castle.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import ru.mashnatash.learning_castle.data.JDBCManager;
import ru.mashnatash.learning_castle.data.userData.PlayerAnswers;
import ru.mashnatash.learning_castle.data.userData.UncheckedTest;
import ru.mashnatash.learning_castle.data.userData.UncheckedTestsMessage;
import ru.mashnatash.learning_castle.tools.JSONManager;

import java.sql.Connection;
import java.util.Arrays;

public class UserActions {

    /*
    STRINGS
     */

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

    public static String getUserName(Connection connection, int id) {
        final JDBCManager manager = new JDBCManager(connection);
        return manager.getNameById(id);
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

    public static String getTestsForChecking(Connection connection, int teacherId) {
        final JDBCManager manager = new JDBCManager(connection);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        UncheckedTestsMessage message = new UncheckedTestsMessage(manager.getNameById(teacherId), manager.getUncheckedTests(teacherId));
        return gson.toJson(message);
    }

    /*
    VOIDS
     */

    public static void testCompletion(Connection connection, String answerData) {
        Gson gson = new Gson();
        PlayerAnswers playerAnswers = new PlayerAnswers();
        playerAnswers = gson.fromJson(answerData, PlayerAnswers.class);
        final JDBCManager manager = new JDBCManager(connection);
        manager.setAnswers(playerAnswers);
    }

    public static void setRecords(Connection connection, JsonObject userData) {
        final JDBCManager manager = new JDBCManager(connection);
        manager.setGameRecord(userData);
    }

    public static void setTestMark(Connection connection, JsonObject userData) {
        final JDBCManager manager = new JDBCManager(connection);
        manager.setTestMark(userData);
    }
}
