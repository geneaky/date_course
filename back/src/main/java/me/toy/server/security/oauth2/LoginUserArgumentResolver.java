package me.toy.server.security.oauth2;

import lombok.RequiredArgsConstructor;
import me.toy.server.entity.LoginUser;
import me.toy.server.security.oauth2.user.UserPrincipal;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        return parameter.getParameterAnnotation(LoginUser.class) != null
                && authentication instanceof UsernamePasswordAuthenticationToken;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken authenticationToken =
                    (UsernamePasswordAuthenticationToken) authentication;
            UserPrincipal userPrincipal = (UserPrincipal) authenticationToken.getPrincipal();
            return userPrincipal.getEmail();
        }
        return Strings.EMPTY;
    }
}
