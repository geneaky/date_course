package me.toy.server.exception.course;

public class AlreadyLikeCourseException extends
    RuntimeException {

  public AlreadyLikeCourseException(String message) {
    super(message);
  }
}
