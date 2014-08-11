package com.chukong.apwebauthentication.dns;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

import com.chukong.apwebauthentication.dns.util.ByteArray;
import com.chukong.apwebauthentication.dns.util.Dns;

public class Ntp {
    private ByteArray ba = null;
    private DatagramSocket ds = null;
    private DatagramPacket send = null;
    private DatagramPacket recv = null;
    private InetAddress server = null;
    private int port = 123;

    public Ntp(InetAddress server) {
        this.server = server;
    }

    public Ntp(String url) {
        server = ByteArray.strtourl(url);
    }

    private void constructRequest() {
        byte[] b = new byte[48];
        b[0] = (byte) 27;
        send = new DatagramPacket(b, 48, server, port);
    }

    public Date getInfo() {
        try {
            ds = new DatagramSocket(8889);
            constructRequest();
            ds.send(send);
            byte[] re = new byte[1024];
            recv = new DatagramPacket(re, 1024);
            ds.receive(recv);
            long ct = ByteArray.bytestoint(re, 32);
            System.out.println(new Date(System.currentTimeMillis()));
            for (int i = 32; i < 40; i++) {
                // System.out.print(((int)(re[i]&127))<<+" ");
            }
            ct -= 2208988800L;
            ct = ct * 1000;
            return new Date(ct);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String args[]) {
        //Dns d = new Dns("pool.ntp.org");
        //System.out.println(new Ntp(d.getIP().get(0)).getInfo());

    }
}