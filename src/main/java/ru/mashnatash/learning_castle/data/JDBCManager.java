package ru.mashnatash.learning_castle.data;

import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JDBCManager {
    private final Connection connection;

    public JDBCManager(Connection connection) {
        this.connection = connection;
    }

    public Map<String,String> authorize(JsonObject userData) {
        System.out.println("Мы в авторизе");
        Map<String, String> answerMap = new HashMap<String, String>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id, userType FROM users WHERE login=? AND u_password=?")){
            //Устанавливаем в нужную позицию значения определённого типа
            System.out.println(userData.get("login").toString());
            preparedStatement.setString(1, Converter.removeQuots(userData.get("login").toString()));
            preparedStatement.setString(2, Converter.removeQuots(userData.get("password").toString()));
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
}
