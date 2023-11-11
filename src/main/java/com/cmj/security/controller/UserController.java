package com.cmj.security.controller;


import com.cmj.security.config.UserPrincipal;
import com.cmj.security.dto.UserDto;
import com.cmj.security.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;

    @RequestMapping("/")
    public String home(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        log.info("userPrincipal : " + userPrincipal);
        return "main";
    }


    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/register")
    @ResponseBody
    public String register(@RequestBody final UserDto userDto) {

        userService.register(userDto);
        return "register";

    }

    @PostMapping("/alert/confirm")
    @ResponseBody
    public ResponseEntity<Void> alertConfirm(HttpSession session) {
        session.removeAttribute("DUPLICATE_LOGIN");
        return ResponseEntity.ok().build();
    }


}
