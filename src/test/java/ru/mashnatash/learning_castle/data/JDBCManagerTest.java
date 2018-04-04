package ru.mashnatash.learning_castle.data;
import com.google.gson.JsonObject;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
