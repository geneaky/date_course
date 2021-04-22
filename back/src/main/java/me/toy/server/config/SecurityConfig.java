package me.toy.server.config;

import lombok.RequiredArgsConstructor;
import me.toy.server.config.handler.OAuth2LoginSuccessHandler;
import me.toy.server.config.jwt.JwtAuthenticationFilter;
import me.toy.server.config.jwt.JwtAuthorizationFilter;
import me.toy.server.config.oauth.PrincipalOauth2UserService;
import me.toy.server.repository.UserRepository;
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
//                .addFilterAfter(new JwtAuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class)
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
//                .and()
//                .formLogin()
//                .loginPage("/loginForm")
//                .loginProcessingUrl("/login")//스프링 시큐리티가 낚아채서 대신 로그인을 진행함
//                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/login")
                .defaultSuccessUrl("/login")
                .userInfoEndpoint()
                .userService(principalOauth2UserService)
                .and()
                .successHandler(new OAuth2LoginSuccessHandler());
    }
}
