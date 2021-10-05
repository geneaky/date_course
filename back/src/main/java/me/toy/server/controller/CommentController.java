package me.toy.server.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.toy.server.annotation.LoginUser;
import me.toy.server.security.UserPrincipal;
import me.toy.server.service.CommentService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Secured("ROLE_USER")
public class CommentController {

  private final CommentService commentService;

  @ApiOperation("데이트 코스 댓글 등록")
  @PostMapping("/courses/{courseId}/comment")
  public void registComment(@PathVariable Long courseId,
      String comment,
      @LoginUser UserPrincipal user) {

    commentService.registComment(courseId, comment, user.getEmail());
  }

  @ApiOperation("데이트 코스 댓글 삭제")
  @DeleteMapping("/courses/{courseId}/comment")
  public void removeComment(@PathVariable Long courseId, @RequestParam("commentId") Long commentId,
      @LoginUser UserPrincipal user) {

    commentService.removeComment(courseId, user.getId(), commentId);
  }
}
