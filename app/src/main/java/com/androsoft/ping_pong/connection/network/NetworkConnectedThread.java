package com.androsoft.ping_pong.connection.network;

import android.util.Log;
import com.androsoft.ping_pong.connection.*;
import com.androsoft.ping_pong.constant.Character;

import java.io.IOException;
import java.net.*;
import java.util.Objects;

public class NetworkConnectedThread implements StreamInterface {
    String local;
    int serverPort;

    public NetworkConnectedThread(String local, int serverPort) {
        this.local = local;
        this.serverPort = serverPort;
    }

    @Override
    public void sendMessage(String data) {
        new Thread() {
            @Override
            public void run() {
                try {
                    DatagramPacket p = new DatagramPacket(data.getBytes(), data.length(), InetAddress.getByName(local), serverPort);
                    new DatagramSocket().send(p);
                } catch (UnknownHostException e) {
                    Log.wtf("UnknownHostException", e.getMessage());
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    Log.wtf("IOException", e.getMessage());
                    throw new RuntimeException(e);
                }
                super.run();
            }
        }.start();
    }
    //todo shoot ve xy kordinatlarını göndermeyi ayarla.


    static DatagramSocket dsocket = null;
    public static void setOnMessageEvent(BattleInterface onMessageEvent) {
        new Thread() {
            @Override
            public void run() {
                try {
                    int port = 11000;

                    dsocket = new DatagramSocket(port);
                    byte[] buffer = new byte[2048];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                    while (true) {
                        dsocket.receive(packet);
                        String data = new String(buffer, 0, packet.getLength());
                        Log.wtf("data", data);
                        packet.setLength(buffer.length);
                        onMessageEvent.message(data, packet.getAddress().getHostAddress());
                    }
                } catch (IOException e) {
                    Log.wtf("UDP ALINIRKEN HATA", e.getMessage());
                    if(e.getMessage() != null && e.getMessage().contains("EADDRINUSE")){
                        dsocket.close();
                        dsocket.disconnect();
                        setOnMessageEvent(onMessageEvent);
                    }
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public static void setOnGameProcess(BattleInterface.OnGameProcess onGameProcess) {
        setOnMessageEvent((data, ipAddress) -> {
            if (data.equals("SHOOT")) {
                onGameProcess.shoot();
                return;
            } else if (data.contains(":") && !data.contains("chr")) {
                String[] xy = data.split(":");
                onGameProcess.xyStatus(Float.parseFloat(xy[0]), Float.parseFloat(xy[1]));
                return;
            } else if(data.equals("paired")){
                onGameProcess.paired();
                return;
            } else if (data.equals("pairedSuccess")) {
                onGameProcess.pairedSuccessfull();
                return;
            }
            Log.wtf("Bilinmeyen data", data);
        });
    }

    public static void setOnBattleInit(BattleInterface.OnBattleInit onBattleInit) {
        setOnMessageEvent((data, ipAddress) -> {
            switch (data) {
                case "find":
                    onBattleInit.onRequest(ipAddress);
                    return;
                case "accept":
                    onBattleInit.catchProcess(ipAddress, true);
                    return;
                case "reject":
                    onBattleInit.catchProcess(ipAddress, false);
                    return;
            }
            if (data.contains("chr:")) {
                onBattleInit.characterSelected(ipAddress, Integer.parseInt(data.replaceAll("chr:", "").trim()));
            }
        });
    }

    public void sendPaired(){
        sendMessage("paired");
    }

    public void sendPairedSuccess(){
        sendMessage("pairedSuccess");
    }

    @Override
    public void findDevice() {
        sendMessage("find");
    }

    @Override
    public void acceptBattle() {
        sendMessage("accept");
    }

    @Override
    public void rejectBattle() {
        sendMessage("reject");
    }

    @Override
    public void sendCharacterInformation(int characterType) {
        sendMessage("chr: " + characterType);
    }

    @Override
    public void sendLocation(float x, float y) {
        sendMessage(x + ":" + y);
    }

    @Override
    public void shoot() {
        sendMessage("SHOOT");
    }
}
