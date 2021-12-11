package com.kalugin.view.client;

import com.kalugin.view.GameMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class GameThread implements Runnable {
    private final BufferedReader input;
    private final BufferedWriter output;
    private final GameClient client;
    private final GameMap map = GameMap.getInstance();

    public GameThread(BufferedReader input, BufferedWriter output, GameClient client) {
        this.input = input;
        this.output = output;
        this.client = client;
    }

    public BufferedReader getInput() {
        return input;
    }

    public BufferedWriter getOutput() {
        return output;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = input.readLine();
//                client.getApplication().appendMessageToChat(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
