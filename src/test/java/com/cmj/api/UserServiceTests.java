package com.cmj.api;


import com.cmj.api.domain.CommonException;
import com.cmj.api.domain.dto.UserDto;
import com.cmj.api.domain.entity.User;
import com.cmj.api.domain.vo.UserVo;
import com.cmj.api.enums.LoginMessage;
import com.cmj.api.repository.UserRepository;
import com.cmj.api.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("이미 존재하는 이메일로 회원가입 시도")
    void signUpWithExistingEmail() {
        // given
        UserDto userDto1 = new UserDto("test01@naver.com", "password123!");
        // when
        userService.signUp(userDto1);
        // then
        UserDto userDto2 = new UserDto("test01@naver.com", "password123!");

        assertThrows(CommonException.class, () -> userService.signUp(userDto2));
    }

    @Test
    @DisplayName("비밀번호 암호화 확인")
    void passwordEncryption() {
        // given
        UserDto userDto = new UserDto("test02@naver.com", "password123!");
        // when
        UserVo user = userService.signUp(userDto);
        // then
        assertThat(bCryptPasswordEncoder.matches("password123!", user.getPassword())).isTrue();
    }

    @Test
    @DisplayName("유효한 UserDto 테스트")
    void validateValidUserDto() {
        // given
        UserDto validUserDto = new UserDto("test03@naver.com", "password123!");
        // when
        Set<ConstraintViolation<UserDto>> violations = validator.validate(validUserDto);
        // then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("잘못된 이메일 형식")
    void validateInvalidEmailUserDto() {
        // given
        UserDto invalidEmailUserDto = new UserDto("invalidEmail", "password123!");
        // when
        Set<ConstraintViolation<UserDto>> violations = validator.validate(invalidEmailUserDto);
        // then
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(LoginMessage.EMAIL_IS_NOT_VALID.getMessage())));
    }

    @Test
    @DisplayName("잘못된 비밀번호 형식")
    void validateInvalidPasswordUserDto() {
        // given
        UserDto invalidPasswordUserDto = new UserDto("test04@naver.com", "pwd");
        // when
        Set<ConstraintViolation<UserDto>> violations = validator.validate(invalidPasswordUserDto);
        // then
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(LoginMessage.PASSWORD_IS_NOT_VALID.getMessage())));
    }

    @Test
    @DisplayName("Null 필드 유효성 검사")
    void validateNullFieldsUserDto() {
        // given
        UserDto nullFieldsUserDto = new UserDto(null, null);
        // when
        Set<ConstraintViolation<UserDto>> violations = validator.validate(nullFieldsUserDto);
        // then
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(LoginMessage.EMAIL_IS_EMPTY.getMessage())));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(LoginMessage.PASSWORD_IS_EMPTY.getMessage())));
    }


    @Test
    @DisplayName("signUp IllegalArgumentException 테스트")
    void signUpThrowsIllegalArgumentExceptionForInvalidUserDto() {
        // given
        UserDto invalidUserDto = new UserDto("invalidEmail", "password123!");
        // when, then
        assertThrows(CommonException.class, () -> userService.signUp(invalidUserDto));
    }

//    @Test
//    @DisplayName("signUp 통합 테스트")
//    void signUpIntegratesWithUserRepository() {
//        UserDto validUserDto = new UserDto("test05@naver.com", "password123!");
//        User expectedUser = User.builder()
//                .id(1L)
//                .email(validUserDto.getEmail())
//                .password(bCryptPasswordEncoder.encode(validUserDto.getPassword()))
//                .build();
//
//        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
//
//        User actualUser = userService.signUp(validUserDto);
//
//        assertEquals(expectedUser, actualUser);
//    }

    @Test
    @DisplayName("전체 유저 조회")
    void findAll() {
        // given
        for (int i = 0; i < 10; i++) {
            UserDto userDto = new UserDto("test0" + i + "@naver.com", "password123!");
            userService.signUp(userDto);
        }

        // when
        List<UserVo> users = userService.findAll();

        // then
        assertEquals(10, users.size());
    }


}