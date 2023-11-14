package com.cmj.api.controller;


import com.cmj.api.config.UserPrincipal;
import com.cmj.api.domain.CommonException;
import com.cmj.api.domain.dto.HealthResponse;
import com.cmj.api.domain.dto.UserDto;
import com.cmj.api.domain.vo.UserVo;
import com.cmj.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

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

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<?> signUp(@RequestBody final UserDto userDto) {

        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.signUp(userDto));
            } catch (CommonException e) {
                return ResponseEntity.status(e.getStatus()).body(e.getMsg());
        }
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<UserVo>> findAll() {
        return ResponseEntity.ok(userService.findAll());
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
