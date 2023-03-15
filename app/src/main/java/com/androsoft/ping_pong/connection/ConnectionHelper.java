package com.androsoft.ping_pong.connection;

import com.androsoft.ping_pong.connection.bluetooth.ConnectedThread;

public interface ConnectionHelper {
    StreamController createConnectedThread() throws Exception;
    String getDevice();
}