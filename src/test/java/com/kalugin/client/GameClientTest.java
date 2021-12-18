package com.kalugin.client;

import org.junit.Test;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class GameClientTest {

    @Test
    public void sendMessageTestExpectedTrue() {
        String message = "Testing client side";
        GameClient gameClient = mock(GameClient.class);
        when(gameClient.sendMessage(message)).thenReturn(true);

        assertTrue(gameClient.sendMessage(message));
    }
}