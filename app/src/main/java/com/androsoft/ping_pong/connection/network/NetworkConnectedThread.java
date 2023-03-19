package com.androsoft.ping_pong.connection.network;

import android.util.Log;
import com.androsoft.ping_pong.connection.OnMessageCaptured;
import com.androsoft.ping_pong.connection.StreamController;

import java.io.IOException;
import java.net.*;

public class NetworkConnectedThread implements StreamController {
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

    public void onMessageEvent(OnMessageCaptured onMessageCaptured) {
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
                        packet.setLength(buffer.length);
                        if(data.equals("SHOOT")){
                            onMessageCaptured.shoot();
                            continue;
                        }
                        String[] xy = data.split(":");
                        onMessageCaptured.xyStatus(Float.parseFloat(xy[0]), Float.parseFloat(xy[1]));
                    }
                } catch (Exception e) {
                    Log.wtf("UDP ALINIRKEN HATA", e.getMessage());

                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }.start();

    }

    @Override
    public void sendLocation(int x, int y) {
        sendMessage(x + ":" + y);
    }

    @Override
    public void shoot() {
        sendMessage("SHOOT");
    }
}
