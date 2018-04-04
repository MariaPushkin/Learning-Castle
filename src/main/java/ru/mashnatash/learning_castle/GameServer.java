package ru.mashnatash.learning_castle;

import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import ru.mashnatash.learning_castle.data.DataPool;
import ru.mashnatash.learning_castle.data.JSONManager;
import ru.mashnatash.learning_castle.data.userData.UserActions;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameServer extends WebSocketServer {
    private static final int TCP_PORT = 16000;

    private Set<WebSocket> simple_connections;
    private Set<WebSocket> connections;
    private Connection dataBaseConnection;

    public GameServer() {
        super(new InetSocketAddress(TCP_PORT));
        simple_connections = new HashSet<WebSocket>();
        connections = Collections.synchronizedSet(simple_connections);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        connections.add(conn);
        System.out.println("New connection from game from " + conn.getRemoteSocketAddress().getAddress().getHostAddress() + " to port " + TCP_PORT);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connections.remove(conn);
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
        if(clientData.get("code").toString().equals("1")) {
            conn.send(UserActions.authorization(dataBaseConnection,true, clientData));
        } else if(clientData.get("code").toString().equals("2")) {
            UserActions.testCompletion(message);
        }

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        //ex.printStackTrace();
        if (conn != null) {
            connections.remove(conn);
            // do some thing if required
        }
        System.out.println("ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }
}
