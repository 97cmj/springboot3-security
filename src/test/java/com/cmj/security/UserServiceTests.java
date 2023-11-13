package com.cmj.security;


import com.cmj.security.domain.entity.User;
import com.cmj.security.dto.UserDto;
import com.cmj.security.repository.UserRepository;
import com.cmj.security.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;

import static com.cmj.security.domain.entity.User.createUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
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

        assertThrows(DataIntegrityViolationException.class, () -> userService.signUp(userDto2));
    }

    @Test
    @DisplayName("비밀번호 암호화 확인")
    void passwordEncryption() {
        // given
        UserDto userDto = new UserDto("test02@naver.com", "password123!");
        // when
        User user = userService.signUp(userDto);
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
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Invalid email address")));
    }

    @Test
    @DisplayName("잘못된 비밀번호 형식")
    void validateInvalidPasswordUserDto() {
        // given
        UserDto invalidPasswordUserDto = new UserDto("test04@naver.com", "pwd");
        // when
        Set<ConstraintViolation<UserDto>> violations = validator.validate(invalidPasswordUserDto);
        // then
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Password must be at least 8 characters long")));
    }

    @Test
    @DisplayName("Null 필드 유효성 검사")
    void validateNullFieldsUserDto() {
        // given
        UserDto nullFieldsUserDto = new UserDto(null, null);
        // when
        Set<ConstraintViolation<UserDto>> violations = validator.validate(nullFieldsUserDto);
        // then
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Email cannot be null, empty or blank")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Password cannot be null, empty or blank")));
    }


    @Test
    @DisplayName("signUp IllegalArgumentException 테스트")
    void signUpThrowsIllegalArgumentExceptionForInvalidUserDto() {
        // given
        UserDto invalidUserDto = new UserDto("invalidEmail", "password123!");
        // when, then
        assertThrows(IllegalArgumentException.class, () -> userService.signUp(invalidUserDto));
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






}