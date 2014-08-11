package com.chukong.apwebauthentication.dns.util;

import java.util.*;
import java.net.*;

public class Dns {
    private Vector<String> answername = null;
    private Vector<InetAddress> answerip = null;

    static byte[] QueryDomain(String domain) {
        String[] StringSplit = domain.split("\\.");
        String DomainFormat = "";
        for (int i = 0; i < StringSplit.length; i++) {
            DomainFormat = DomainFormat
                    + (Character.toString((char) StringSplit[i].length()) + StringSplit[i]);
        }
        return (DomainFormat + Character.toString((char) 0)).getBytes();
    }

    public void opet() {
        InetAddress url = null;
        DatagramSocket ds = null;
        try {
            url = ByteArray.strtourl("202.97.224.68");
        } catch (Exception urlException) {
            System.out.println(urlException);
        }
        try {
            ds = new DatagramSocket(8888);
        } catch (Exception BlindError) {
            System.out.println(BlindError + "udp�޷�ʹ�ö˿�");
        }
        byte[] b = new byte[1024];
        DatagramPacket dpr = new DatagramPacket(b, 1024);
        short Flags = 0x0100;
        short Questions = 0x0001;
        short AnswerRRs = 0x0000;
        short AuthorityRRs = 0x0000;
        short AdditionalRRs = 0x0000;
        short SEARCHTYPE = 1;
        short SEARCHCLASS = 0x0001; // Class һ��Ϊ 1 ָ Internet
        // ����������
        ByteArray ba = new ByteArray();
        ba.add((short) 20);
        ba.add(Flags);
        ba.add(Questions);
        ba.add(AnswerRRs);
        ba.add(AuthorityRRs);
        ba.add(AdditionalRRs);
        ba.add(QueryDomain("www.baidu.com"));
        ba.add(SEARCHTYPE);
        ba.add(SEARCHCLASS);
        byte[] Header = ba.getBytes();
        ba = null;
        try {
            ds.setSoTimeout(6000);
            ds.send(new DatagramPacket(Header, Header.length, url, 53));
            ds.receive(dpr);

        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("TID is OK");
        if ((ByteArray.bytes2short(b, 2) & 0x000f) != 0) {
            System.out
                    .print("Error the domain you request is can't to explain!!");
        }
        answername = new Vector<String>();
        answerip = new Vector<InetAddress>();
        int answers = ByteArray.bytes2short(b, 6);
        System.out.println("Answers:" + answers);
        int answercount = 0;
        // [4:6]Questions �� �����ֽ�
        // [6:8]Answer RRs�� �����ֽ�
        // [8:10]Authority RRs�� �����ֽ�
        // [10:12]Additional RRs�� �����ֽ�
        int bitflag = 12;
        while (b[bitflag] != 0)
            bitflag++;
        bitflag++; // �������
        bitflag += 2; // ���Type
        bitflag += 2; // ���Class
        int i = 0;
        while (i < answers) {
            System.out.println("============" + (i + 1) + "==============");
            // Name �����ֽ� ָ��-ָ��������ַ�
            bitflag += 2;
            if (ByteArray.bytes2short(b, bitflag) == 1) {
                // Type �����ֽ�
                bitflag += 8; // Class�����ֽ�
                // TTL �ĸ��ֽ�
                bitflag += 2; // Rdatalength �����ֽ�
                System.out.println(ByteArray.unsignedByte(b[bitflag]) + "."
                        + ByteArray.unsignedByte(b[bitflag + 1]) + "."
                        + ByteArray.unsignedByte(b[bitflag + 2]) + "."
                        + ByteArray.unsignedByte(b[bitflag + 3]));
                answerip.add(ByteArray.strtourl(ByteArray
                        .unsignedByte(b[bitflag])
                        + "."
                        + ByteArray.unsignedByte(b[bitflag + 1])
                        + "."
                        + ByteArray.unsignedByte(b[bitflag + 2])
                        + "."
                        + ByteArray.unsignedByte(b[bitflag + 3])));
                bitflag += 4;
            } else if (ByteArray.bytes2short(b, bitflag) == 5) // CNAME
            {
                bitflag += 8;
                int rdatalength = ByteArray.bytes2short(b, bitflag);
                bitflag += 2;
                StringBuffer domain = new StringBuffer();
                getDomain(b, bitflag, domain);
                System.out.println(domain.toString());
                answername.add(domain.toString());
                bitflag += rdatalength;
            }
            i++;
        }

    }

    public void getDomain(byte[] b, int offset, StringBuffer domain) {
        if ((b[offset] & 0xc0) == 0xc0) {
            getDomain(b, ByteArray.bytes2short(b, offset) & 0x3fff, domain);
        } else if (b[offset] == 0) {
        } else {
            domain.append(new String(b, offset + 1, ByteArray
                    .unsignedByte(b[offset])) + ".");
            getDomain(b, offset + 1 + ByteArray.unsignedByte(b[offset]), domain);
        }
    }

}
