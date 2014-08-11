package com.chukong.apwebauthentication.dns;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class DNS {

    private String queryDomain = "";
    private String dnsIP = "127.0.0.1";

    public static void main(String[] args) throws UnknownHostException {
        // InetAddress address = Str2InetAddress("192.168.3.234");
        // System.out.println(address);

        DNS dns = new DNS();
        dns.AnalysisDNS();
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

    public void AnalysisDNS() {
        DatagramSocket ds = null;
        InetAddress dnsurl = Str2InetAddress(dnsIP);
        byte[] SendBuf = DNSQueryPackage.pkgDNS("www.baidu.com");
        DatagramPacket dp = new DatagramPacket(SendBuf, SendBuf.length, dnsurl,
                53);
        byte[] ReceiveBuf = new byte[1024];
        try {
            ds = new DatagramSocket(8564);
            ds.setSoTimeout(5000);
            ds.send(dp);
            DatagramPacket dpr = new DatagramPacket(ReceiveBuf,
                    ReceiveBuf.length);
            ds.receive(dpr);
            int len = dpr.getLength();
            for (int i = 0; i < len; i++) {
                System.out.print(ReceiveBuf[i] + " ");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int n = 12;
        while (ReceiveBuf[n] != 0)
            n++;

    }
}
