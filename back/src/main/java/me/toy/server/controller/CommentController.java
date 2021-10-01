package me.toy.server.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.toy.server.annotation.LoginUser;
import me.toy.server.service.CommentService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Secured("ROLE_USER")
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/courses/{courseId}/comment")
  @ApiOperation("데이트 코스에 댓글 등록")
  public void registComment(@PathVariable Long courseId,
      String comment,
      @LoginUser String userEmail) {

    commentService.registComment(courseId, comment, userEmail);
  }
}
