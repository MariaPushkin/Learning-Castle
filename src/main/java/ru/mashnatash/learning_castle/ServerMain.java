package ru.mashnatash.learning_castle;


public class ServerMain {
    public static void main(String[] args) {
        WebsiteServer ws = new WebsiteServer();
        ws.setConnectionLostTimeout(30);
        ws.start();

        GameServer gs = new GameServer();
        gs.setConnectionLostTimeout(30);
        gs.start();
    }
}
