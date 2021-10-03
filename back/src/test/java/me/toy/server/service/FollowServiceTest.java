package me.toy.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import me.toy.server.dto.user.UserRequestDto.FollowRequest;
import me.toy.server.dto.user.UserRequestDto.UnfollowRequest;
import me.toy.server.dto.user.UserResponseDto.UserFollowees;
import me.toy.server.dto.user.UserResponseDto.UserFollowers;
import me.toy.server.entity.Follow;
import me.toy.server.entity.User;
import me.toy.server.exception.user.AlreadyFollowUserException;
import me.toy.server.exception.user.AlreadyUnfollowUserException;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.FollowRepository;
import me.toy.server.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

  @Mock
  UserRepository userRepository;
  @Mock
  FollowRepository followRepository;

  @InjectMocks
  FollowService followService;

  private User createUser() {
    return User.builder()
        .id(1L)
        .email("test@naver.com")
        .build();
  }

  @Test
  @DisplayName("사용자 팔로우를 성공")
  public void addFollowerInUserFollowersTest() throws Exception {

    User user = createUser();
    User targetUser = User.builder()
        .id(3L)
        .email("target@naver.com")
        .build();
    FollowRequest followRequest = new FollowRequest(targetUser.getId());

    when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
    when(userRepository.findById(any())).thenReturn(Optional.of(targetUser));

    followService.followUser(followRequest, user.getEmail());

    verify(followRepository).save(any());
    assertThat(user.getFollows().size()).isEqualTo(1);
  }

  @Test
  @DisplayName("인가받지 않은 사용자가 팔로우 시도시 예외가 발생한다.")
  public void followUserWithUnAuthorizedUserTest() throws Exception {

    FollowRequest followRequest = mock(FollowRequest.class);

    when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      followService.followUser(followRequest, "NONO@naver.com");
    });
  }

  @Test
  @DisplayName("존재하지 않는 사용자를 팔로우 시도시 예외가 발생한다.")
  public void followNotExistedUserTest() throws Exception {

    User mockedUser = mock(User.class);
    FollowRequest followRequest = new FollowRequest(13L);

    when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(mockedUser));
    when(userRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      followService.followUser(followRequest, "OKOK@naver.com");
    });
  }

  @Test
  @DisplayName("이미 팔로우 상태인 사용자를 팔로우 시도시 예외가 발생한다.")
  public void AlreadyFollowUserFollowTest() throws Exception {

    User user = createUser();
    User mockedTargetUser = mock(User.class);
    Follow mockedFollow = mock(Follow.class);
    FollowRequest followRequest = new FollowRequest(13L);

    when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(user));
    when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockedTargetUser));
    when(followRepository.findByUserIdAndFolloweeId(any(), any())).thenReturn(
        Optional.ofNullable(mockedFollow));

    assertThrows(AlreadyFollowUserException.class, () -> {
      followService.followUser(followRequest, user.getEmail());
    });
  }

  @Test
  @DisplayName("팔로우하는 사용자들을 반환")
  public void getUserFollowingUsersTest() throws Exception {

    User user = createUser();
    List<User> users = new ArrayList<>();
    User user1 = User.builder()
        .email("other@naver.com")
        .build();
    User user2 = User.builder()
        .email("people@naver.com")
        .build();
    users.add(user1);
    users.add(user2);

    when(userRepository.findFollowees(any())).thenReturn(users);

    UserFollowees userFollowingUsers = followService.getUserFollowees(user.getId());

    assertThat(userFollowingUsers.getFolloweeDtos().size()).isEqualTo(2);
  }

  @Test
  @DisplayName("사용자 팔로우 목록에서 선택한 팔로우 사용자를 언팔로우")
  public void removeFollowerInUserFollowersTest() throws Exception {

    User user = createUser();
    User followUser = User.builder()
        .id(3L)
        .build();
    Follow follow = new Follow(user, followUser.getId());

    UnfollowRequest unfollowRequest = new UnfollowRequest(
        follow.getFolloweeId());

    when(userRepository.findById(followUser.getId())).thenReturn(Optional.of(followUser));
    when(followRepository.findByUserIdAndFolloweeId(any(), any())).thenReturn(Optional.of(follow));

    followService.unfollowUser(unfollowRequest, user.getId());

    verify(followRepository).delete(any());
  }

  @Test
  @DisplayName("팔로우하고 있지 않는 사용자 팔로우 취소시 예외가 발생한다.")
  public void unfollowAlreadyUnfollowUserTest() throws Exception {

    User followee = mock(User.class);
    UnfollowRequest unfollowRequest = new UnfollowRequest(1L);

    when(userRepository.findById(any())).thenReturn(Optional.ofNullable(followee));
    when(followRepository.findByUserIdAndFolloweeId(any(), any())).thenReturn(Optional.empty());

    assertThrows(AlreadyUnfollowUserException.class, () -> {
      followService.unfollowUser(unfollowRequest, 1L);
    });
  }

  @Test
  @DisplayName("사용자를 팔로우하는 팔루워 목록 반환")
  public void getUserFollowersUsersTest() throws Exception {

    User user = createUser();
    User user1 = User.builder()
        .id(2L)
        .email("other@naver.com")
        .build();
    User user2 = User.builder()
        .id(3L)
        .email("people@naver.com")
        .build();
    Follow follow1 = new Follow(user1, user.getId());
    Follow follow2 = new Follow(user2, user.getId());

    List<User> list = new ArrayList<>();
    list.add(user1);
    list.add(user2);

    when(userRepository.findFollowers(any())).thenReturn(list);

    UserFollowers userFollowersUsers = followService.getUserFollowers(user.getId());

    assertThat(userFollowersUsers.getFollowerDtos().size()).isEqualTo(2);
  }
}