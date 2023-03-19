package com.androsoft.ping_pong.connection;

public interface StreamController {
    void sendMessage(String data);
    void onMessageEvent(OnMessageCaptured onMessageCaptured);

    void sendLocation(int x, int y);
    void shoot();
}
