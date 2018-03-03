package ru.mashnatash.learning_castle.data;

import com.google.gson.JsonObject;

import java.sql.Connection;

public class UserActions {

    public static String authorization(Connection connection, String message) {
            JsonObject userData = JSONManager.toJsonObject(message);
            final JDBCManager manager = new JDBCManager(connection);
            return JSONManager.toJsonString(manager.authorize(userData));
    }
}
