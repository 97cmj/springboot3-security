package com.cmj.security;


import com.cmj.security.domain.entity.User;
import com.cmj.security.domain.entity.UserRole;
import com.cmj.security.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.IntStream;

@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    @DisplayName("100더미생성")
    void insertDummies(){
        //1-80까지는 USER만 지정,
        //81-90까지는 USER,MANAGER,
        ///91-100까지는 USER,MANAGER,ADMIN
        IntStream.rangeClosed(1,100).forEach(i->{
            User user = User.builder()
                    .email("user"+i+"@naver.com")
                    .password(bCryptPasswordEncoder.encode("1111"))
                    .build();
            //default role
            userRepository.save(user);

        });
    }
}