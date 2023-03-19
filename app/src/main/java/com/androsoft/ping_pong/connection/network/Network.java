package com.androsoft.ping_pong.connection.network;

import com.androsoft.ping_pong.connection.ConnectionHelper;
import com.androsoft.ping_pong.connection.StreamController;
import com.androsoft.ping_pong.util.Device;

public class Network implements ConnectionHelper {
    private String ipAddress;

    public Network(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public StreamController createConnectedThread() throws Exception {
        return new NetworkConnectedThread(
                (Device.getDeviceName().contains("SM-G780Gk")) ?
                        "172.30.146.0" :
                    "172.30.163.106"
                ,11000);
    }

    @Override
    public String getDevice() {
        return ipAddress;
    }
}
