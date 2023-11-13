package com.cmj.security.service;

import com.cmj.security.domain.entity.User;
import com.cmj.security.dto.UserDto;
import com.cmj.security.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User signUp(UserDto userDto) {

        checkExistingEmail(userDto.getEmail());
        validateUserDto(userDto);

        User user = User.createUser(userDto.getEmail(), bCryptPasswordEncoder.encode(userDto.getPassword()));


        User savedUser = userRepository.save(user);

        log.info("savedUser: {}", savedUser);

        return savedUser;
    }

    private void checkExistingEmail(String email) {

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            throw new DataIntegrityViolationException("이미 가입되어 있는 유저입니다.");
        }
    }

    private void validateUserDto(UserDto userDto) {

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<UserDto> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            throw new IllegalArgumentException("UserDto validation error(s): " + sb.toString());
        }
    }


}
