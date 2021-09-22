package me.toy.server.config;

import lombok.RequiredArgsConstructor;
import me.toy.server.entity.User;
import me.toy.server.repository.UserRepository;
import me.toy.server.security.auth.CustomAuthenticationFilter;
import me.toy.server.security.auth.handler.CustomAuthenticationFailureHandler;
import me.toy.server.security.auth.handler.CustomAuthenticationSuccessHandler;
import me.toy.server.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import me.toy.server.security.oauth2.handler.CustomOAuth2AuthenticationFailureHandelr;
import me.toy.server.security.oauth2.handler.CustomOAuth2AuthenticationSuccessHandler;
import me.toy.server.security.oauth2.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Optional;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends
    WebSecurityConfigurerAdapter {

  private final CustomOAuth2UserService customOAuth2UserService;
  private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
  private final CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;
  private final CustomOAuth2AuthenticationFailureHandelr customOAuth2AuthenticationFailureHandelr;
  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
  private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
  private final UserRepository userRepository;

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {

    return super.authenticationManagerBean();
  }

  @PostConstruct
  @Profile("test")
  public void settingUserTest() {

    User user = new User();
    user.setEmail("test@naver.com");
    user.setName("testUser");
    userRepository.save(user);
  }

  @PreDestroy
  @Profile("test")
  public void clearSettingUserTest() {

    Optional<User> testUser = userRepository.findByEmail("test@naver.com");

    userRepository.delete(testUser.get());
  }

  @Bean
  public CustomAuthenticationFilter getFilter() throws Exception {
    CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(
        userRepository);
    customAuthenticationFilter.setAuthenticationManager(authenticationManager());
    return customAuthenticationFilter;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.csrf().disable();
    http.cors();
    http.httpBasic();

    http.authorizeRequests()
        .antMatchers("/user/**").access("hasRole('ROLE_USER')")
        .antMatchers("/datecourse/*/**").access("hasRole('ROLE_USER')")
        .antMatchers("/v2/api-docs", "/swagger-ui/**", "/swagger-resources/**", "/webjars/**")
        .permitAll()
        .antMatchers("/auth/**", "/oauth2/**", "/datecourse/*", "/signUp").permitAll()
        .anyRequest().authenticated();

    http.addFilterAt(getFilter(), UsernamePasswordAuthenticationFilter.class)
        .formLogin()
        .usernameParameter("email")
        .passwordParameter("password")
        .loginProcessingUrl("/signIn")
        .permitAll()
        .successHandler(customAuthenticationSuccessHandler)
        .failureHandler(customAuthenticationFailureHandler);

    http.logout().disable();
    http.sessionManagement()
        .maximumSessions(1)
        .maxSessionsPreventsLogin(false)
        .expiredUrl("http://localhost:3000/login")
        .and()
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

    http.oauth2Login()
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
        .successHandler(customOAuth2AuthenticationSuccessHandler)
        .failureHandler(customOAuth2AuthenticationFailureHandelr);

  }

}
