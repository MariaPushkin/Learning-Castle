package ru.mashnatash.learning_castle.data;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


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
}
