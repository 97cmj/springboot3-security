package com.cmj.api.service;

import com.cmj.api.domain.CommonException;
import com.cmj.api.domain.dto.UserDto;
import com.cmj.api.domain.entity.User;
import com.cmj.api.domain.vo.UserVo;
import com.cmj.api.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public UserVo signUp(UserDto userDto) {

        checkExistingEmail(userDto.getEmail());
        validateUserDto(userDto);

        User user = User.builder()
                .email(userDto.getEmail())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        return UserVo.builder().id(savedUser.getId())
                .email(savedUser.getEmail())
                .password(savedUser.getPassword())
                .build();

    }

    private void checkExistingEmail(String email) {

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            throw new CommonException("이미 가입되어 있는 유저입니다.", HttpStatus.BAD_REQUEST);
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
            throw new CommonException(sb.toString(), HttpStatus.BAD_REQUEST);
        }
    }


    public List<UserVo> findAll() {
        return userRepository.findAll().stream()
                .map(UserVo::new)
                .collect(Collectors.toList());
    }
}
