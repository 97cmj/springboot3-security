package com.cmj.security.controller;


import com.cmj.security.config.UserPrincipal;
import com.cmj.security.dto.UserDto;
import com.cmj.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;

    @RequestMapping("/")
    public ModelAndView home(ModelAndView view,
                       @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal != null) {
            log.info("userPrincipal : " + userPrincipal);
        }

        view.addObject("title", "메인 페이지");
        view.setViewName("main");
        
        return view;
    }


    @RequestMapping("/login")
    public ModelAndView login(ModelAndView view) {

        view.addObject("title", "로그인 페이지");
        view.setViewName("login");

        return view;

    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Void> register(@RequestBody final UserDto userDto) {

        userService.register(userDto);

        return ResponseEntity.ok().build();

    }

}
