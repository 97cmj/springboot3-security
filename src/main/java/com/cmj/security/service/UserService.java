package com.cmj.security.service;

import com.cmj.security.domain.entity.User;
import com.cmj.security.domain.entity.UserRole;
import com.cmj.security.dto.UserDto;
import com.cmj.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void register(UserDto userDto) {

        User user = User.builder()
                .email(userDto.getEmail())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .build();

        userRepository.save(user);

    }



}
