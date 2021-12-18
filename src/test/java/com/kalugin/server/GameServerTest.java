package com.kalugin.server;

import com.kalugin.client.GameClient;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;


public class GameServerTest {

    @Test
    public void sendMessageTest() throws IOException {
        String message = "Testing server side";

        GameServer gameServer = new GameServer();
        gameServer.start();
        GameClient gameClient = new GameClient();
        gameClient.start();

        gameClient.sendMessage(message);

        assertEquals(message, gameServer.getMessage());

        gameClient.stop();
        gameServer.stop();
    }
}