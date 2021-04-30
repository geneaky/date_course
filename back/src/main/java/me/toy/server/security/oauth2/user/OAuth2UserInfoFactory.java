package me.toy.server.security.oauth2.user;

import me.toy.server.exception.OAuth2AuthenticationProcessingException;
import me.toy.server.security.oauth2.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String,Object> attributes){
        if(registrationId.equalsIgnoreCase(AuthProvider.google.name())){
            return new GoogleOAuth2UserInfo(attributes);
        }else{
            throw new OAuth2AuthenticationProcessingException("Login is not supported yes");
        }
    }
}