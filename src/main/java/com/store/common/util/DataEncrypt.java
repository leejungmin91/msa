package com.store.common.util;

import org.springframework.security.crypto.codec.Hex;

import java.security.MessageDigest;

public class DataEncrypt {
    MessageDigest md;
    String strSRCData = "";
    String strENCData = "";
    String strOUTData = "";

    public DataEncrypt(){ }
    public static String encrypt(String strData){
        String passACL = null;
        MessageDigest md = null;
        try{
            md = MessageDigest.getInstance("SHA-256");
            md.reset();
            md.update(strData.getBytes());
            byte[] raw = md.digest();
            passACL = encodeHex(raw);
        }catch(Exception e){
            System.out.print("암호화 에러" + e.toString());
        }
        return passACL;
    }

    public static String encodeHex(byte[] b){
        char [] c = Hex.encode(b);
        return new String(c);
    }
}
