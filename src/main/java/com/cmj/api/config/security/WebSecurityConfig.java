package com.cmj.api.config.security;

import com.cmj.api.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import javax.sql.DataSource;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Log4j2
public class WebSecurityConfig {

    private final UserDetailService userDetailsService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthFailureHandler customAuthFailureHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final DataSource dataSource;


    //스프링 시큐리티 보안 필터 체인 무시 설정
    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring()
                .requestMatchers("/static/**", "/favicon.ico"));
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("SESSION"); // 쿠키 이름 설정
        serializer.setCookiePath("/"); // 쿠키 경로 설정
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); // 도메인 패턴 설정
        serializer.setUseSecureCookie(true); // HTTPS를 통해서만 쿠키 전송
        serializer.setUseHttpOnlyCookie(true); // JavaScript를 통한 쿠키 접근 방지
        serializer.setSameSite("strict"); // CSRF 공격 방지
        serializer.setCookieMaxAge(60 * 60 * 24); // 쿠키 만료 시간 설정(초 단위)

        return serializer;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/signup", "/login","/list", "/auth").anonymous()
                        .requestMatchers("/actuator/**").permitAll()
//                        .requestMatchers(new IpAddressRequestMatcher("192.168.1.1")).authenticated()
                        .anyRequest().authenticated())

                .formLogin((formLogin) -> formLogin.loginPage("/login")
                        .loginProcessingUrl("/auth")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/", true)
                        .failureHandler(customAuthFailureHandler))

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true) // 세션 초기화
                        .deleteCookies("SESSION", "remember-me")

                ).csrf(AbstractHttpConfigurer::disable

                ).httpBasic(AbstractHttpConfigurer::disable)


                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionFixation().changeSessionId()
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션이 필요하면 생성하도록 셋팅
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .sessionRegistry(sessionRegistry())
                        .expiredUrl("/login"))

                .rememberMe(rememberMe -> rememberMe
                        .key("rememberMeKey")
                        .tokenRepository(persistentTokenRepository())
                        .rememberMeServices(rememberMeServices(persistentTokenRepository()))
                        .userDetailsService(userDetailsService))


                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(customAuthenticationEntryPoint))

                ;


        return http.build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices(PersistentTokenRepository tokenRepository) {
        PersistentTokenBasedRememberMeServices rememberMeServices = new
                PersistentTokenBasedRememberMeServices("rememberMeKey", userDetailsService, tokenRepository);
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
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
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
