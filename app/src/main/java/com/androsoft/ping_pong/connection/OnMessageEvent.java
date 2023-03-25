package com.androsoft.ping_pong.connection;

public interface OnMessageEvent {
    void message(String data, String ipAddress) throws EndConnection;
}
