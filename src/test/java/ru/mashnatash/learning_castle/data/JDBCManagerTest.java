package ru.mashnatash.learning_castle.data;
import com.google.gson.JsonObject;
import org.junit.Test;
import ru.mashnatash.learning_castle.data.userData.PlayerAnswers;
import ru.mashnatash.learning_castle.data.userData.QuestionInfo;
import ru.mashnatash.learning_castle.data.userData.UserActions;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class JDBCManagerTest {

    @org.junit.Test
    public void getCoursesTest() {
        String message = "{\"id\" : \"3\"}";
        JsonObject clientData = JSONManager.toJsonObject(message);
        Connection dataBaseConnection;
        try {
            dataBaseConnection = DataPool.getInstance().getConnection();
            final JDBCManager manager = new JDBCManager(dataBaseConnection);
            System.out.println(manager.getCourses(clientData).gerString());
        } catch (PropertyVetoException|IOException|SQLException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void getTestTest() {
        int  topic = 1;
        Connection dataBaseConnection;
        try {
            dataBaseConnection = DataPool.getInstance().getConnection();
            System.out.println(UserActions.getTest(dataBaseConnection,topic));
        } catch (PropertyVetoException|IOException|SQLException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void setTestTest() {
        PlayerAnswers playerAnswers = new PlayerAnswers(2);

        Connection dataBaseConnection;
        try {
            dataBaseConnection = DataPool.getInstance().getConnection();
            final JDBCManager manager = new JDBCManager(dataBaseConnection);
            manager.setAnswers(playerAnswers);
        } catch (PropertyVetoException|IOException|SQLException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void checkIsCompleteTest() {
        int topic = 2;
        int id = 10;
        Connection dataBaseConnection;
        try {
            dataBaseConnection = DataPool.getInstance().getConnection();
            final JDBCManager manager = new JDBCManager(dataBaseConnection);
            manager.checkIsComplete(topic,id);
        } catch (PropertyVetoException|IOException|SQLException e) {
            e.printStackTrace();
        }
    }
}
