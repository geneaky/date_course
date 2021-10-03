package me.toy.server.controller;

import static me.toy.server.dto.user.UserRequestDto.UserRegisterForm;

import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.toy.server.annotation.LoginUser;
import me.toy.server.dto.user.UserResponseDto.UserDto;
import me.toy.server.security.UserPrincipal;
import me.toy.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Secured("ROLE_USER")
public class UserController {

  private final UserService userService;

  @ApiOperation("사용자 회원가입")
  @PostMapping("/signUp")
  public void registerUser(@Valid @RequestBody UserRegisterForm userRegisterForm,
      HttpServletResponse response)
      throws IOException {

    userService.createUserAccount(userRegisterForm);
    response.sendRedirect("http://localhost:3000");
  }

  @ApiOperation("로그인한 사용자 정보 제공")
  @GetMapping("/info")
  public ResponseEntity<UserDto> getUserInfo(@LoginUser UserPrincipal user) {

    return ResponseEntity.ok().body(userService.getUserInfo(user.getEmail()));
  }

}
