package com.androsoft.ping_pong.connection.network;

import android.util.Log;
import com.androsoft.ping_pong.connection.ConnectionHelper;
import com.androsoft.ping_pong.connection.StreamController;
import com.androsoft.ping_pong.connection.bluetooth.ConnectedThread;
import com.androsoft.ping_pong.util.Game;

public class Network implements ConnectionHelper {
    private String ipAddress;

    public Network(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public StreamController createConnectedThread() throws Exception {
        Log.wtf("dd",Game.DEVICE_NAME);
        return new NetworkConnectedThread(
                (Game.DEVICE_NAME.contains("S20 FE")) ?
                        "172.30.146.0" :
                    "172.30.137.59"
                ,11000);
    }

    @Override
    public String getDevice() {
        return ipAddress;
    }
}
