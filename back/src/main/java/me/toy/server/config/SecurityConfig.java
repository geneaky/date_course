package me.toy.server.config;

import lombok.RequiredArgsConstructor;
import me.toy.server.entity.User;
import me.toy.server.repository.UserRepository;
import me.toy.server.security.jwt.TokenAuthenticationFilter;
import me.toy.server.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import me.toy.server.security.handler.CustomAuthenticationFailureHandelr;
import me.toy.server.security.handler.CustomAuthenticationSuccessHandler;
import me.toy.server.security.oauth2.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,jsr250Enabled = true,prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter { //이 클래스는 스프링 시큐리티 필터이고 이 필터가 스프링 필터체인에 등록이된다.

    private final CustomOAuth2UserService customOAuth2UserService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandelr customAuthenticationFailureHandelr;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final UserRepository userRepository;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @PostConstruct
    @Profile("dev")
    public void settingUserTest(){
        User user = new User();
        user.setEmail("test@naver.com");
        user.setName("testUser");
        userRepository.save(user);
    }

    @PreDestroy
    @Profile("dev")
    public void clearSettingUserTest(){
        Optional<User> testUser = userRepository.findByEmail("test@naver.com");
        userRepository.delete(testUser.get());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.formLogin().disable();
        http.httpBasic().disable();

        http.authorizeRequests()
                    .antMatchers("/user/**").access("hasRole('ROLE_USER')")
                    .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                    .antMatchers("/datecourse/like/**").access("hasRole('ROLE_USER')")
                    .antMatchers("/user/saved/**").access("hasRole('ROLE_USER')")
                    .antMatchers("/v2/api-docs","/swagger-ui/**","/swagger-resources/**","/webjars/**").permitAll()
                    .antMatchers("/auth/**","/oauth2/**","/datecourse/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .oauth2Login()
                    .authorizationEndpoint()
                        .baseUri("/oauth2/authorize")
                        .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                        .and()
                    .redirectionEndpoint()
                        .baseUri("/oauth2/callback/*")
                        .and()
                .userInfoEndpoint()
                    .userService(customOAuth2UserService)
                    .and()
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandelr);

        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
