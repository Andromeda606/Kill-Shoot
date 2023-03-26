package com.androsoft.ping_pong.connection.network;

import android.util.Log;
import com.androsoft.ping_pong.connection.*;

import java.io.IOException;
import java.net.*;

public class NetworkConnectedThread implements StreamInterface {
    String local;
    int serverPort;

    public NetworkConnectedThread(String local, int serverPort){
        this.local = local;
        this.serverPort = serverPort;
    }

    @Override
    public void sendMessage(String data) {
        new Thread(){
            @Override
            public void run() {
                try {
                    DatagramPacket p = new DatagramPacket(data.getBytes(), data.length(), InetAddress.getByName(local), serverPort);
                    new DatagramSocket().send(p);
                } catch (UnknownHostException e) {
                    Log.wtf("UnknownHostException",e.getMessage());
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    Log.wtf("IOException",e.getMessage());
                    throw new RuntimeException(e);
                }
                super.run();
            }
        }.start();
    }
    //todo shoot ve xy kordinatlarını göndermeyi ayarla.


    public static void setOnMessageEvent(BattleInterface onMessageEvent) {
        new Thread(){
            @Override
            public void run() {
                try {
                    int port = 11000;

                    DatagramSocket dsocket = new DatagramSocket(port);
                    byte[] buffer = new byte[2048];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                    while (true) {
                        dsocket.receive(packet);
                        String data = new String(buffer, 0, packet.getLength());
                        Log.wtf("data", data);
                        packet.setLength(buffer.length);
                        onMessageEvent.message(data, packet.getAddress().getHostAddress());
                    }
                } catch (EndConnection ignored){
                    Log.wtf("Uyarı","Bağlantı izlemesi kapatıldı.");
                } catch (Exception e) {
                    Log.wtf("UDP ALINIRKEN HATA", e.getMessage());

                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }.start();

    }

    public static void setOnGameProcess(BattleInterface.OnGameProcess onGameProcess){
        setOnMessageEvent((data, ipAddress) -> {
            if(data.equals("SHOOT")){
                onGameProcess.shoot();
                return;
            } else if (data.contains(":")) {
                String[] xy = data.split(":");
                onGameProcess.xyStatus(Float.parseFloat(xy[0]), Float.parseFloat(xy[1]));
                return;
            }
            Log.wtf("Bilinmeyen data", data);
            throw new EndConnection();
        });
    }

    public static void setOnBattleInit(BattleInterface.OnBattleInit onBattleInit){
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
            throw new EndConnection();
        });
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
    public void sendAcceptRequest(int characterType) {
        sendMessage("accept: " + characterType);
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
