package ru.mashnatash.learning_castle.data;


import org.junit.Test;
import org.junit.runner.RunWith;

import javax.jws.soap.SOAPBinding;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class JDBCStorageTest {

    @org.junit.Test
    public void testCreate() throws Exception {
        final JDBCStorage storage = new JDBCStorage();
        final int id = storage.add(new User(-1,"ihopefinaltest"));
        final User user = storage.get(id);
        assertEquals(id, user.getId());
        /*final User user2 = storage.get(4);
        System.out.println(user2.getName());*/
        storage.close();
    }

    @org.junit.Test
    public void values() {
        final JDBCStorage storage = new JDBCStorage();
        List<User> users = new ArrayList<User>();
        users = storage.values();
        assertEquals("Kola", users.get(0).getName());
        assertEquals("Tony", users.get(1).getName());
    }

    @org.junit.Test
    public void add() {
    }
}
