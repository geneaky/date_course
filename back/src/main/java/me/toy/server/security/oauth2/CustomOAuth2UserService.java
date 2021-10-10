package me.toy.server.security.oauth2;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.toy.server.entity.User;
import me.toy.server.exception.user.OAuth2AuthenticationProcessingException;
import me.toy.server.repository.UserRepository;
import me.toy.server.security.UserPrincipal;
import me.toy.server.security.oauth2.user.OAuth2UserInfo;
import me.toy.server.security.oauth2.user.OAuth2UserInfoFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest)
      throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

    try {
      return processOAuth2User(oAuth2UserRequest, oAuth2User);
    } catch (AuthenticationException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest,
      OAuth2User oAuth2User) {
    String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
    Map<String, Object> attributes = oAuth2User.getAttributes();
    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
        .getOAuth2UserInfo(registrationId, attributes);

    if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
      throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 Provider");
    }

    Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
    User user;

    if (userOptional.isPresent()) {
      user = userOptional.get();
      if (!user.getProvider().equals(
          OAuth2Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
        throw new OAuth2AuthenticationProcessingException("you can use this account to login");
      }
      user = updateExistingUser(user, oAuth2UserInfo);
    } else {
      user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
    }

    return UserPrincipal.create(user, oAuth2User.getAttributes());
  }

  private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
    User user = User.builder()
        .provider(
            OAuth2Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
        .providerId(oAuth2UserInfo.getId())
        .name(oAuth2UserInfo.getName())
        .email(oAuth2UserInfo.getEmail())
        .imageUrl(oAuth2UserInfo.getImageUrl()).build();

    return userRepository.save(user);
  }

  private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
    existingUser.updateUserName(oAuth2UserInfo.getName());
    existingUser.updateImageUrl(oAuth2UserInfo.getImageUrl());

    return userRepository.save(existingUser);
  }
}
