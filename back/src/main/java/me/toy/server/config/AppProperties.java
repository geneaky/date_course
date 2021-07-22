package me.toy.server.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

  private final Auth auth = new Auth();
  private final OAuth2 oauth2 = new OAuth2();

  public Auth getAuth() {
    return auth;
  }

  public OAuth2 getOauth2() {
    return oauth2;
  }

  public static class Auth {

    private String tokenSecret;
    private long tokenExpirationMsec;

    public String getTokenSecret() {
      return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
      this.tokenSecret = tokenSecret;
    }

    public long getTokenExpirationMsec() {
      return tokenExpirationMsec;
    }

    public void setTokenExpirationMsec(long tokenExpirationMsec) {
      this.tokenExpirationMsec = tokenExpirationMsec;
    }
  }

  public static final class OAuth2 {

    private List<String> authorizedRedirectUris = new ArrayList<>();

    public List<String> getAuthorizedRedirectUris() {
      return authorizedRedirectUris;
    }

    //인가된 uri리스트만 받아서 검증하고 싶을때 사용하면된다. naver,google,kakao이런 uri에서만 redirect token받고싶을때 지정하는것
    //지금은 당장 쓸일이 없지만 미래를 위해
    public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
      this.authorizedRedirectUris = authorizedRedirectUris;
      return this;
    }
  }
}
