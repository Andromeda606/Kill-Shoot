package com.androsoft.killshot.connection.network;

import com.androsoft.killshot.connection.ConnectionHelper;
import com.androsoft.killshot.connection.StreamInterface;

public class Network implements ConnectionHelper {
    private final String ipAddress;

    public Network(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public StreamInterface createConnectedThread() {
        return new NetworkConnectedThread(ipAddress, 11000);
    }

    @Override
    public String getDevice() {
        return ipAddress;
    }
}
