package me.toy.server.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.toy.server.dto.user.UserRequestDto.FollowRequest;
import me.toy.server.dto.user.UserRequestDto.UnfollowRequest;
import me.toy.server.dto.user.UserResponseDto.FolloweeDto;
import me.toy.server.dto.user.UserResponseDto.FollowerDto;
import me.toy.server.dto.user.UserResponseDto.UserFollowees;
import me.toy.server.dto.user.UserResponseDto.UserFollowers;
import me.toy.server.entity.Follow;
import me.toy.server.entity.User;
import me.toy.server.exception.user.AlreadyFollowUserException;
import me.toy.server.exception.user.AlreadyUnfollowUserException;
import me.toy.server.exception.user.UserNotFoundException;
import me.toy.server.repository.FollowRepository;
import me.toy.server.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

  private final UserRepository userRepository;
  private final FollowRepository followRepository;

  @Transactional
  public void followUser(FollowRequest followRequest, String userEmail) {

    User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
        new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다."));
    User followee = userRepository.findById(followRequest.getFolloweeId())
        .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));
    if (followRepository.findByUserIdAndFolloweeId(user.getId(), followee.getId())
        .isPresent()) {
      throw new AlreadyFollowUserException("이미 팔로우 상태 입니다.");
    }

    Follow follow = new Follow(user, followee.getId());
    followRepository.save(follow);
  }

  @Transactional
  public void unfollowUser(UnfollowRequest unfollowRequest,
      Long userId) {

    User followee = userRepository.findById(unfollowRequest.getFolloweeId()).orElseThrow(() ->
        new UserNotFoundException("없는 사용자 입니다."));

    Follow follow = followRepository.findByUserIdAndFolloweeId(userId,
        followee.getId()).orElseThrow(() ->
        new AlreadyUnfollowUserException("해당 유저를 팔로우 하고 있지 않습니다."));

    followRepository.delete(follow);
  }

  @Transactional(readOnly = true)
  public UserFollowees getUserFollowees(
      Long userId) {

    List<User> followees = userRepository.findFollowees(userId);
    List<FolloweeDto> followeeDtos = followees.stream()
        .map(u -> new FolloweeDto(u.getId(), u.getName(), u.getEmail())).collect(
            Collectors.toList());

    return new UserFollowees(followeeDtos);
  }

  @Transactional(readOnly = true)
  public UserFollowers getUserFollowers(Long userId) {

    List<User> followers = userRepository.findFollowers(userId);

    List<FollowerDto> followerDtos = followers.stream()
        .map(u -> new FollowerDto(u.getId(), u.getName(), u.getEmail()))
        .collect(Collectors.toList());

    return new UserFollowers(followerDtos);
  }

}
