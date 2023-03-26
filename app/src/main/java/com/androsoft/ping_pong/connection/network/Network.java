package com.androsoft.ping_pong.connection.network;

import com.androsoft.ping_pong.connection.ConnectionHelper;
import com.androsoft.ping_pong.connection.StreamInterface;

public class Network implements ConnectionHelper {
    private String ipAddress;

    public Network(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public StreamInterface createConnectedThread() throws Exception {
        return new NetworkConnectedThread(ipAddress, 11000);
    }

    @Override
    public String getDevice() {
        return ipAddress;
    }
}
