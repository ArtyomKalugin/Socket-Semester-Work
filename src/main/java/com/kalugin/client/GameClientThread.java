package com.kalugin.client;

import com.kalugin.view.GameMap;
import com.kalugin.client.GameClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class GameClientThread implements Runnable{
    private final BufferedReader input;
    private final BufferedWriter output;
    private final GameClient client;
    private final GameMap map = GameMap.getInstance();
    private String name;

    public GameClientThread(BufferedReader input, BufferedWriter output, GameClient client, String name) {
        this.input = input;
        this.output = output;
        this.client = client;
        this.name = name;
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

                if(message != null) {
                    String[] directions = message.split(" ");

                    if(directions[0].equals("new")) {
                        map.addOpp(name, directions[1]);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
