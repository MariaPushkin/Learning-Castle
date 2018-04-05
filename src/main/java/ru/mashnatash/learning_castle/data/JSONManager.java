package ru.mashnatash.learning_castle.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map;

public class JSONManager {
    public static JsonObject toJsonObject(String message) {
        JsonParser parser = new JsonParser();
        return parser.parse(message).getAsJsonObject();
    }

    public static String toJsonString(Map<String,String> message) {
        /*Map<String, String> myMap = new HashMap<String, String>();
        myMap.put("one", "hello");
        myMap.put("two", "world");*/
        Gson gson = new GsonBuilder().create();
        return gson.toJson(message);
    }

    public static String removeQuots(String str) {
        return str.substring(1, str.length() - 1);
    }

    public static String[] devideQuestion(String str) {
        //Отделить вопрос от ответов
        String[]result=str.split("\n");
        result[0] = result[0].replace("question: ", "");
        for(int i = 1; i < result.length;i++) {
            result[i] = result[i].replace("answer: ","");
            result[i] = result[i].replace("answer_right: ","");
        }
        return result;
    }
}
