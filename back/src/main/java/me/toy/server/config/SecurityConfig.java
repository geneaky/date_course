package me.toy.server.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.toy.server.config.handler.OAuth2LoginSuccessHandler;
import me.toy.server.config.jwt.JwtAuthenticationFilter;
import me.toy.server.config.jwt.JwtAuthorizationFilter;
import me.toy.server.config.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import me.toy.server.config.oauth.PrincipalOauth2UserService;
import me.toy.server.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter { //이 클래스는 스프링 시큐리티 필터이고 이 필터가 스프링 필터체인에 등록이된다.

    private final PrincipalOauth2UserService principalOauth2UserService;
    private final UserRepository userRepository;
    private final CorsFilter corsFilter;

    private final HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .addFilterAfter(new JwtAuthorizationFilter(authenticationManager(),userRepository),BasicAuthenticationFilter.class);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter)
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/user/**").authenticated()//로그인한 사용자가 들어올수 있는거고 뒤에 이걸 붙이면 역할에 따른 권한 부여access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization/*")
                .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository)
                .and()
                .redirectionEndpoint()
                .baseUri("/login/oauth2/code/*")
                .and()
                .userInfoEndpoint()
                .userService(principalOauth2UserService)
                .and()
                .successHandler(new OAuth2LoginSuccessHandler());
    }
}
