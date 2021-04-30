package me.toy.server.config;

import lombok.RequiredArgsConstructor;
import me.toy.server.security.jwt.JwtAuthenticationFilter;
import me.toy.server.security.jwt.JwtTokenProvider;
import me.toy.server.security.oauth2.user.CustomUserDetailService;
import me.toy.server.security.handler.CustomAuthenticationFailureHandelr;
import me.toy.server.security.handler.CustomAuthenticationSuccessHandler;
import me.toy.server.security.jwt.JwtAuthorizationFilter;
import me.toy.server.security.oauth2.CustomOAuth2UserService;
import me.toy.server.repository.UserRepository;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,jsr250Enabled = true,prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter { //이 클래스는 스프링 시큐리티 필터이고 이 필터가 스프링 필터체인에 등록이된다.

    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.formLogin().disable();
        http.httpBasic().disable();

        http.addFilter(new JwtAuthenticationFilter());
        http.addFilter(new JwtAuthorizationFilter(authenticationManager(),userRepository,jwtTokenProvider));

        http.authorizeRequests()
                    .antMatchers("/user/**").access("hasRole('ROLE_USER')")
                    .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                    .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(new CustomAuthenticationSuccessHandler())
                .failureHandler(new CustomAuthenticationFailureHandelr());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties clientProperties){
        List<ClientRegistration> registrations =
                clientProperties.getRegistration().keySet().stream()
                .map(provider -> getRegistration(clientProperties, provider))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(OAuth2ClientProperties clientProperties,String provider){
        if("google".equals(provider)){
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("google");

            return CommonOAuth2Provider.GOOGLE.getBuilder(provider)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .scope("email","profile")
                    .build();
        }
        return null;
    }
}
