package com.chukong.apwebauthentication.dns;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class DNSServerOld {

    /**
     * @param args
     */

    private static String dnsIP = "8.8.8.8";

    public static void main(String[] args) {
        // DNSServer.start();
    }

    public static void start() {
        DatagramSocket dsServer = null;
        DatagramSocket dsClient = null;

        try {
            dsServer = new DatagramSocket(53);
            dsClient = new DatagramSocket(9527);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int n = 0;
        while (true) {

            byte[] bufServer = new byte[20480];
            byte[] bufClient = new byte[20480];
            DatagramPacket dpServer = new DatagramPacket(bufServer,
                    bufServer.length);
            try {
                dsServer.receive(dpServer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            InetAddress clienturl = dpServer.getAddress();
            int port = dpServer.getPort();
            int len = dpServer.getLength();
            InetAddress dnsurl = Str2InetAddress(dnsIP);
            DatagramPacket dpClient = new DatagramPacket(bufServer, len,
                    dnsurl, 53);
            DatagramPacket dpr = new DatagramPacket(bufClient, bufClient.length);
            try {
                dsClient.send(dpClient);
                dsClient.receive(dpr);
                DatagramPacket dps = new DatagramPacket(bufClient,
                        dpr.getLength(), clienturl, port);
                dsServer.send(dps);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("......" + (++n));
        }
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
