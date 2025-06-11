package com.store.member.util;

import org.jasypt.util.text.BasicTextEncryptor;

public class CryptoService {

    private final BasicTextEncryptor encryptor;

    public CryptoService(BasicTextEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    public String encrypt(String plainText) {
        return encryptor.encrypt(plainText);
    }

    public String decrypt(String encryptedText) {
        return encryptor.decrypt(encryptedText);
    }

    public String safeDecrypt(String encryptedText) {
        try {
            return encryptor.decrypt(encryptedText);
        } catch (Exception e) {
            // 복호화 실패 시 null 반환
            return null;
        }
    }
}
