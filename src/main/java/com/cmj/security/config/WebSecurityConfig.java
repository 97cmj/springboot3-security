package com.cmj.security.config;

import com.cmj.security.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Log4j2
public class WebSecurityConfig {

    private final UserDetailService userDetailsService;


    //스프링 시큐리티 보안 필터 체인 무시 설정
    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring()
                .requestMatchers("/static/**", "/favicon.ico"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/register", "/login", "/auth").anonymous()
                        .anyRequest().authenticated())

                .formLogin((formLogin) -> formLogin.loginPage("/login")
                        .loginProcessingUrl("/auth")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/", true)
                        .failureHandler(new CustomAuthFailureHandler()))

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")

                ).csrf(AbstractHttpConfigurer::disable

                ).httpBasic(AbstractHttpConfigurer::disable)


                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionFixation().migrateSession()
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션이 필요하면 생성하도록 셋팅
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                        .sessionRegistry(sessionRegistry())
                        .expiredUrl("/login"))

                .rememberMe(rememberMe -> rememberMe
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(3600)
                        .alwaysRemember(false)
                        .userDetailsService(userDetailsService));

        return http.build();
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        authProvider.setHideUserNotFoundExceptions(false);
        return authProvider;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        log.info("SessionRegistry");
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        log.info("HttpSessionEventPublisher");
        return new HttpSessionEventPublisher();
    }
}
