package ru.mashnatash.learning_castle.data;

import org.junit.Test;
import ru.mashnatash.learning_castle.tools.JSONManager;

import java.util.HashMap;
import java.util.Map;


public class JSONManagerTest {

    @Test
    public void toJsonObject() {
    }

    @Test
    public void toJsonString() {
        Map<String, String> myMap = new HashMap<String, String>();
        myMap.put("one", "hello");
        myMap.put("two", "world");

        System.out.println(JSONManager.toJsonString(myMap));
    }

    @Test
    public void getAnswerRight() {
        String desc = "question: Выберите наибольшее из данных чисел: A - 8,5; B - 2sqrt19; C - 3sqrt8; D - sqrt73\n" +
                "answer: A\n" +
                "answer_right: B\n" +
                "answer: C\n" +
                "answer: D";
        System.out.println(JSONManager.getRightAnswer(desc));
    }
}
