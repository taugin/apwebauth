package com.chukong.apwebauthentication.dns;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class DNSDispatcher extends Thread {

    private byte[] buf = new byte[20480];
    private InetAddress ip = null;
    private int port = 0;
    private int len = 0;
    private static int myport = 50000;
    private static String dnsIP = "8.8.8.8";

    public DNSDispatcher(byte[] b, int len, InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
        this.len = len;
        for (int i = 0; i < len; i++)
            buf[i] = b[i];
    }

    public void run() {
        DatagramSocket dsClient = null;
        DatagramSocket dsServer = null;
        byte[] bufReceive = new byte[20480];
        try {
            dsClient = new DatagramSocket(myport++);
            dsServer = new DatagramSocket(myport++);
            if (myport > 60000)
                myport = 50000;
        } catch (SocketException e) {
            e.printStackTrace();
        }

        InetAddress InetDNSIp = Str2InetAddress(dnsIP);
        DatagramPacket dpClient = new DatagramPacket(buf, len, InetDNSIp, 53);
        DatagramPacket dpReceive = new DatagramPacket(bufReceive,
                bufReceive.length);
        try {
            dsClient.send(dpClient);
            dsClient.receive(dpReceive);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DatagramPacket dpServer = new DatagramPacket(bufReceive,
                dpReceive.getLength(), ip, port);

        try {
            dsServer.send(dpServer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        dsClient.close();
        dsServer.close();
    }

    public static InetAddress Str2InetAddress(String url) {
        byte[] Url = new byte[4];
        String[] url_arr = url.split("\\.");
        if (url_arr.length == 4) {
            Url[0] = (byte) Integer.parseInt(url_arr[0]);
            Url[1] = (byte) Integer.parseInt(url_arr[1]);
            Url[2] = (byte) Integer.parseInt(url_arr[2]);
            Url[3] = (byte) Integer.parseInt(url_arr[3]);
            try {
                return InetAddress.getByAddress(Url);
            } catch (java.net.UnknownHostException e) {
                System.out.println(e);
            }
        }
        return null;
    }
}