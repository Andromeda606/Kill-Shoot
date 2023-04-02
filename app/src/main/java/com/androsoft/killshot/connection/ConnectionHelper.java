package com.androsoft.killshot.connection;

public interface ConnectionHelper {
    StreamInterface createConnectedThread() throws Exception;
    String getDevice();
}