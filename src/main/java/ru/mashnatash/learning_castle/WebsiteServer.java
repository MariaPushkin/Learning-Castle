package ru.mashnatash.learning_castle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class WebsiteServer extends WebSocketServer {
    private static final int TCP_PORT = 4444;

    private Set<WebSocket> connections;

    public WebsiteServer() {
        super(new InetSocketAddress(TCP_PORT));
        connections = new HashSet<WebSocket>();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        connections.add(conn);
        System.out.println("New connection from website from " + conn.getRemoteSocketAddress().getAddress().getHostAddress() + " to port " + TCP_PORT);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connections.remove(conn);
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Message from client: " + message);
        /*for (WebSocket sock : connections) {
            sock.send(message);
        }*/
        JsonParser parser = new JsonParser();
        JsonObject mainObject = parser.parse(message).getAsJsonObject();
        //JsonArray login = mainObject.getAsJsonArray("login");
        System.out.println("Login: " + mainObject.get("login"));
        System.out.println("Password " + mainObject.get("password"));
        String st = mainObject.get("login").toString();
        System.out.println(st);
        if(mainObject.get("login").toString().equals("\"student\"") && mainObject.get("password").toString().equals("\"1234\"")) {
            conn.send("{\"status\": \"OK\", \"rights\": \"student\"}");
        } else if (mainObject.get("login").toString().equals("\"teacher\"") && mainObject.get("password").toString().equals("\"1234\"")) {
            conn.send("{\"status\": \"OK\", \"rights\": \"teacher\"}");
        } else {
            conn.send("{\"status\": \"NOT OK\"}");
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
