package com.cmj.security.controller;


import com.cmj.security.config.UserPrincipal;
import com.cmj.security.domain.HealthResponse;
import com.cmj.security.dto.UserDto;
import com.cmj.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;
    private final Environment env;

    @RequestMapping("/")
    public ModelAndView home(ModelAndView view,
                             @AuthenticationPrincipal UserPrincipal userPrincipal) throws UnknownHostException {

        if (userPrincipal != null) {
            log.info("userPrincipal : " + userPrincipal);
        }

        HealthResponse response = getHealthInfo();

        log.info("response : " + response);

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

    @PostMapping("/signUp")
    @ResponseBody
    public ResponseEntity<Void> signUp(@RequestBody final UserDto userDto) {

        userService.signUp(userDto);

        return ResponseEntity.ok().build();

    }


    public HealthResponse getHealthInfo() throws UnknownHostException {
        RestTemplate restTemplate = new RestTemplate();

        String http = "http://";
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("local.server.port");
        String endPoint = "/actuator/health";
        String healthEndpoint = http + ip + ":" + port + endPoint;

        ResponseEntity<HealthResponse> responseEntity = restTemplate.getForEntity(healthEndpoint, HealthResponse.class);

        return responseEntity.getBody();
    }

}
