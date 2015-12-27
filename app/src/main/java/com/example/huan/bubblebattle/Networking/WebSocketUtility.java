package com.example.huan.bubblebattle.Networking;

import android.util.Log;
import com.example.huan.bubblebattle.MessageHandler;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketUtility {
    //singleton -> private constructor
    private WebSocketUtility() {
    }

    private static MessageHandler messageHandler;
    private static final String logCat = WebSocketUtility.class.toString();
    final static String address = "ws://192.168.43.153:8080/BubbleBattle/gamelobby";

    private static WebSocketClient client;

    public static void setMessageHandler(MessageHandler handler) {
        messageHandler = handler;
    }

    public static WebSocketClient getClient() {
        if (client == null) {
            URI uri;
            try {
                uri = new URI(address);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return null;
            }

            client = new WebSocketClient(uri, new Draft_17()) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Log.d(logCat, "websocket opened");
                }

                @Override
                public void onMessage(final String s) {
                    final String message = s;
                    messageHandler.handleMessage(s);
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    Log.d(logCat, "Closed " + s);
                }

                @Override
                public void onError(Exception e) {
                    Log.e(logCat, "Error " + e.getMessage());
                }
            };
        }
        //else: client already created
        return client;
    }
}
