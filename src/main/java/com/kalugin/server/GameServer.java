package com.kalugin.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    private static final int PORT = 5555;
    private ServerSocket socket;
    private final List<GameServerThread> clients = new ArrayList<>();


    public void start() throws IOException {
        socket = new ServerSocket(PORT);

        while (true) {
            Socket clientSocket = socket.accept();

            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));

            GameServerThread gameServerThread = new GameServerThread(input, output, this);
            clients.add(gameServerThread);

            new Thread(gameServerThread).start();
        }
    }

    public void sendMessage(String message, GameServerThread sender) throws IOException {
        for (GameServerThread client : clients) {
            if (client.equals(sender)){
                continue;
            }

            client.getOutput().write(message+ "\n");
            client.getOutput().flush();
        }
    }

    public void removeClient(GameServerThread gameServerThread) {
        clients.remove(gameServerThread);
    }

    public static void main(String[] args) throws IOException {
        GameServer gameServer = new GameServer();
        gameServer.start();
    }
}
