package ru.mashnatash.learning_castle.data;
import com.google.gson.JsonObject;
import org.junit.Test;
import ru.mashnatash.learning_castle.data.userData.QuestionInfo;
import ru.mashnatash.learning_castle.data.userData.UserActions;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class JDBCManagerTest {

    @org.junit.Test
    public void getCourses() {
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
    public void getTest() {
        int  topic = 1;
        Connection dataBaseConnection;
        try {
            dataBaseConnection = DataPool.getInstance().getConnection();
            //final JDBCManager manager = new JDBCManager(dataBaseConnection);
            //QuestionInfo[] info = manager.getTestQuestions(topic);
            System.out.println(UserActions.getTest(dataBaseConnection,topic));
            //System.out.println(manager.getTestQuestions(topic)[0].getQuestion());
            //luckyNumbers = Arrays.copyOf(luckyNumbers, 2 * luckyNumbers.length);
        } catch (PropertyVetoException|IOException|SQLException e) {
            e.printStackTrace();
        }
    }
}
