package ru.mashnatash.learning_castle;

import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import ru.mashnatash.learning_castle.data.*;
import ru.mashnatash.learning_castle.data.userData.UserActions;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.*;

public class WebsiteServer extends WebSocketServer  {
    private static final int TCP_PORT = 4444;

    private Set<WebSocket> unsafeSocketConnections;
    private Set<WebSocket> socketConnections;
    private Connection dataBaseConnection;

    public WebsiteServer() {
        super(new InetSocketAddress(TCP_PORT));
        unsafeSocketConnections = new HashSet<WebSocket>();
        //connections = new HashSet<WebSocket>();
        socketConnections = Collections.synchronizedSet(unsafeSocketConnections);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        socketConnections.add(conn);
        System.out.println("New connection from website from " + conn.getRemoteSocketAddress().getAddress().getHostAddress() + " to port " + TCP_PORT);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        socketConnections.remove(conn);
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Message from client: " + message);
        try {
            dataBaseConnection = DataPool.getInstance().getConnection();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JsonObject clientData = JSONManager.toJsonObject(message);
        //TODO 1: Заменить на свич
        if(clientData.get("code").toString().equals("1")) {
            conn.send(UserActions.authorization(dataBaseConnection, false, clientData));
        } else if (clientData.get("code").toString().equals("2")) {
            conn.send("Воронков Петр Петрович");
        } else if (clientData.get("code").toString().equals("3")) {
            conn.send((UserActions.getTeacherCourses(dataBaseConnection, clientData)));
        } else if (clientData.get("code").toString().equals("4")) {
            //System.out.println(UserActions.getCourseResultTable(dataBaseConnection, clientData.get("course_id").getAsInt()));

            conn.send(UserActions.getCourseResultTable(dataBaseConnection, clientData.get("course_id").getAsInt()));
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        //ex.printStackTrace();
        if (conn != null) {
            socketConnections.remove(conn);
            // do some thing if required
        }
        System.out.println("ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }
}
