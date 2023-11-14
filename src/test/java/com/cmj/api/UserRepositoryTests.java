//package com.cmj.security;
//
//import com.cmj.security.domain.entity.User;
//import com.cmj.security.dto.UserDto;
//import com.cmj.security.repository.UserRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//// ... 다른 import 문 ...
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//
//@DataJpaTest
//@ExtendWith(SpringExtension.class)
//@TestPropertySource(locations = "classpath:test_application.yml")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Import(UserRepositoryTests.Config.class)
//public class UserRepositoryTests {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    // ... 테스트 코드 ...
//
//    static class Config {
//
//        @Bean
//        public BCryptPasswordEncoder bCryptPasswordEncoder() {
//            return new BCryptPasswordEncoder();
//        }
//    }
//
//    @Test
//    public void signUp() {
//
//        UserDto userDto = UserDto.builder()
//                .email("test1@naver.com")
//                .password("password123!")
//                .build();
//
//        User user = User.createUser(userDto.getEmail(), bCryptPasswordEncoder.encode(userDto.getPassword()));
//
//        User savedUser = userRepository.save(user);
//
//        Assertions.assertThat(savedUser.getEmail()).isEqualTo("test1@naver.com");
//        Assertions.assertThat(bCryptPasswordEncoder.matches("password123!", savedUser.getPassword())).isTrue();
//
//
//
//
//    }
//}
