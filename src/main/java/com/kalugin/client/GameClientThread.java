package com.kalugin.client;

import com.kalugin.view.GameMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class GameClientThread implements Runnable{
    private final BufferedReader input;
    private final BufferedWriter output;
    private final GameClient client;
    private final GameMap map = GameMap.getInstance();

    public GameClientThread(BufferedReader input, BufferedWriter output, GameClient client) {
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

                if(message != null) {
                    String[] directions = message.split(" ");

                    if(directions[0].equals("new")) {
                        map.createNewOpp(directions[1]);
                    }

                    if (directions[0].equals("move")) {
                        double x = Double.parseDouble(directions[2]);
                        double y = Double.parseDouble(directions[3]);

                        int animationX = Integer.parseInt(directions[4]);
                        int animationY = Integer.parseInt(directions[5]);

                        map.moveOpp(directions[1], x, y, animationX, animationY);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
