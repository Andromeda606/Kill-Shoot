package com.androsoft.ping_pong.connection;

public interface ConnectionHelper {
    StreamInterface createConnectedThread() throws Exception;
    String getDevice();
}