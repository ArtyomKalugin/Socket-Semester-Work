package com.kalugin.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class GameClient {
    private Socket socket;
    private GameClientThread gameThread;

    public void sendMessage(String message) {
        try {
            gameThread.getOutput().write(message);
            gameThread.getOutput().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        socket = new Socket("127.0.0.1", 5555);

        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

        gameThread = new GameClientThread(input, output, this);

        new Thread(gameThread).start();
    }
}
