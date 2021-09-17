package me.toy.server.security.oauth2.user;

import me.toy.server.exception.user.OAuth2AuthenticationProcessingException;
import me.toy.server.security.oauth2.OAuth2Provider;

import java.util.Map;

public class OAuth2UserInfoFactory {

  public static OAuth2UserInfo getOAuth2UserInfo(String registrationId,
      Map<String, Object> attributes) {
    if (registrationId.equalsIgnoreCase(OAuth2Provider.google.toString())) {
      return new GoogleOAuth2UserInfo(attributes);
    } else {
      throw new OAuth2AuthenticationProcessingException("Login is not supported yes");
    }
  }
}
