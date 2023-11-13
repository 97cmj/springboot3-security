package com.cmj.security;

import com.cmj.security.dto.UserDto;
import com.cmj.security.repository.UserRepository;
import com.cmj.security.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@Rollback
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    @DisplayName("회원가입 API 테스트")
    @Test
    void signUp(){
        //given
        UserDto userDto = UserDto.builder()
                .email("test01@naver.com")
                .password("password123!")
                .build();

        //when
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/signUp", userDto, Void.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }
}
