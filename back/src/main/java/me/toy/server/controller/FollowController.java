package me.toy.server.controller;

import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.toy.server.annotation.LoginUser;
import me.toy.server.dto.user.UserRequestDto;
import me.toy.server.dto.user.UserResponseDto.UserFollowees;
import me.toy.server.dto.user.UserResponseDto.UserFollowers;
import me.toy.server.security.UserPrincipal;
import me.toy.server.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Secured("ROLE_USER")
public class FollowController {

  private final FollowService followService;

  @ApiOperation("사용자 팔로우")
  @PostMapping("/followees")
  public void followUser(@Valid @RequestBody UserRequestDto.FollowRequest followRequest,
      @LoginUser UserPrincipal user) {

    followService.followUser(followRequest, user.getEmail());
  }

  @ApiOperation("사용자 언팔로우")
  @DeleteMapping("/followees")
  public void unfollowUser(@Valid @RequestBody UserRequestDto.UnfollowRequest unfollowRequest,
      @LoginUser UserPrincipal user) {

    followService.unfollowUser(unfollowRequest, user.getId());
  }

  @ApiOperation("사용자 팔로잉 조회")
  @GetMapping("/followees")
  public ResponseEntity<UserFollowees> getUserFollowings(
      @LoginUser UserPrincipal user) {

    return ResponseEntity.ok().body(followService.getUserFollowees(user.getId()));
  }

  @ApiOperation("사용자 팔로워 조회")
  @GetMapping("/followers")
  public ResponseEntity<UserFollowers> getUserFollowers(
      @LoginUser UserPrincipal user) {

    return ResponseEntity.ok().body(followService.getUserFollowers(user.getId()));
  }
}
