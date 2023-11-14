package com.cmj.api;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.junit.jupiter.api.Test;

public class JasyptConfigTest {

        @Test
        void stringEncryptor() {
            String url = "url";
            String username = "username";
            String password = "password";
            String key = "key";

            System.out.println(jasyptEncoding(url));
            System.out.println(jasyptEncoding(username));
            System.out.println(jasyptEncoding(password));
            System.out.println(jasyptEncoding(key));
        }

        public String jasyptEncoding(String value) {

            String key = "mySecretKey";
            StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
            pbeEnc.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
            pbeEnc.setPassword(key);
            pbeEnc.setIvGenerator(new RandomIvGenerator());
            return pbeEnc.encrypt(value);
        }
}
