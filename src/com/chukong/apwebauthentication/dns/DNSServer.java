package com.chukong.apwebauthentication.dns;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class DNSServer {

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        DatagramSocket dsServer = null;

        try {
            dsServer = new DatagramSocket(53);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        byte[] buf = new byte[20480];
        int n = 0;
        while (true) {
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            try {
                dsServer.receive(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            new DNSDispatcher(buf, dp.getLength(), dp.getAddress(),
                    dp.getPort()).start();
            System.out.println(++n + ".......");
        }
    }
}
