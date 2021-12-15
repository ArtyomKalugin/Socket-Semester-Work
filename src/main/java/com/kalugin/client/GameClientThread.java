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

                    if (directions[0].equals("new")) {
                        map.createNewOpp(directions[1]);
                    }

                    if (directions[0].equals("move")) {
                        double x = Double.parseDouble(directions[2]);
                        double y = Double.parseDouble(directions[3]);

                        int animationIndex = Integer.parseInt(directions[4]);
                        int animationColumns = Integer.parseInt(directions[5]);

                        int animationWidth = Integer.parseInt(directions[6]);
                        int animationHeight = Integer.parseInt(directions[7]);

                        int animationOffsetX = Integer.parseInt(directions[8]);
                        int animationOffsetY = Integer.parseInt(directions[9]);

                        int hp = Integer.parseInt(directions[10]);

                        map.moveOpp(directions[1], x, y, animationIndex, animationColumns, animationWidth, animationHeight,
                                animationOffsetX, animationOffsetY, hp);
                    }

                    if (directions[0].equals("shoot")) {
                        boolean isRight = false;
                        String rotation = directions[2];
                        if (rotation.equals("right")) {
                            isRight = true;
                        }

                        int damage = Integer.parseInt(directions[3]);
                        double x = Double.parseDouble(directions[4]);
                        double y = Double.parseDouble(directions[5]);

                        map.createBullet(isRight, damage, x, y);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
