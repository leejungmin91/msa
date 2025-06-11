package com.store.member.config;

import com.store.member.util.CryptoService;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class EncryptionConfig {

    @Value("${encryptor.key}")
    private String key;

    @Bean
    public BasicTextEncryptor basicTextEncryptor() {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(key);
        return encryptor;
    }

    @Bean
    public CryptoService cryptoService(BasicTextEncryptor encryptor) {
        return new CryptoService(encryptor);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
