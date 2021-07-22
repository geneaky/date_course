package me.toy.server.dto;

import lombok.Data;
import me.toy.server.entity.Comment;

@Data
public class DateCourseCommentDto {

  private String userName;

  private String commentContent;

  public DateCourseCommentDto(Comment comment) {
    this.userName = comment.getUser().getName();
    this.commentContent = comment.getCommentContents();
  }
}
