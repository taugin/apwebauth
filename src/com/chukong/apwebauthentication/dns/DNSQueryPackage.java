package com.chukong.apwebauthentication.dns;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DNSQueryPackage {

    /**
     * @param args
     */
    private static byte[] id = { 0x12, 0x56 };
    private static byte[] flag = { 0x01, 0x00 };
    private static byte[] Questions = { 0x00, 0x01 };
    private static byte[] Answers = { 0x00, 0x00 };
    private static byte[] Authority = { 0x00, 0x00 };
    private static byte[] Additional = { 0x00, 0x00 };
    private static byte[] SearchType = { 0x00, 0x01 };
    private static byte[] SearchClass = { 0x00, 0x01 };

    public static byte[] pkgDNS(String queryDomain) {
        if (queryDomain == null)
            return null;
        ByteArrayOutputStream QPKGBytes = new ByteArrayOutputStream();
        try {
            QPKGBytes.write((id));
            QPKGBytes.write((flag));
            QPKGBytes.write((Questions));
            QPKGBytes.write((Answers));
            QPKGBytes.write((Authority));
            QPKGBytes.write((Additional));
            QPKGBytes.write(Domain2Bytes(queryDomain));
            QPKGBytes.write((SearchType));
            QPKGBytes.write((SearchClass));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return QPKGBytes.toByteArray();
    }

    public static byte[] Domain2Bytes(String queryDomain) {
        String[] StringSplit = queryDomain.split("\\.");
        String DomainFormat = "";
        for (int i = 0; i < StringSplit.length; i++) {
            DomainFormat = DomainFormat
                    + (Character.toString((char) StringSplit[i].length()) + StringSplit[i]);
        }
        return (DomainFormat + Character.toString((char) 0)).getBytes();
    }

    public static byte[] Short2Bytes(short n) {
        byte[] b = new byte[2];
        b[1] = (byte) (n & 0xff);
        b[0] = (byte) (n >> 8 & 0xff);
        return b;
    }

    // public static short Bytes2Short(byte[] b){
    // short st = 0;
    // st = (short)(st & 0x0000);
    // st = (short)(st & b[1]);
    // st = (short)(st << 8);
    // st = (short)(st & b[0]);
    // return st;
    // }

    public static void main(String[] args) {
        byte[] b = Domain2Bytes("www.baidu.com");
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i] + " ");
        }
        System.out.println();
        System.out.println("0".getBytes());
    }
}
