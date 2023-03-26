package com.androsoft.ping_pong.connection;

public interface StreamInterface {
    void sendMessage(String data);

    void findDevice();
    void acceptBattle();
    void rejectBattle();

    void sendAcceptRequest(int characterType);
    void sendLocation(float x, float y);
    void shoot();
}
