package com.androsoft.ping_pong.connection;

public interface StreamController {
    void sendMessage(String data);

    void findDevice();

    void sendAcceptRequest(int characterType);
    void sendLocation(float x, float y);
    void shoot();
}
