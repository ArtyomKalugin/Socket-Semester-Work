package com.kalugin.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.SocketException;

public class GameServerThread implements Runnable {
    private final BufferedReader input;
    private final BufferedWriter output;
    private final GameServer server;
    private boolean isWorking = true;

    public GameServerThread(BufferedReader input, BufferedWriter output, GameServer server) {
        this.input = input;
        this.output = output;
        this.server = server;
    }

    public BufferedReader getInput() {
        return input;
    }

    public BufferedWriter getOutput() {
        return output;
    }

    public GameServer getServer() {
        return server;
    }

    public void stop() {
        isWorking = false;
    }

    @Override
    public void run() {
        try {
            while (isWorking) {
                String message = input.readLine();
                server.sendMessage(message, this);
            }
        } catch (SocketException socketException) {
            server.removeClient(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
