package me.toy.server.exception.user;

public class AlreadyFollowUserException extends RuntimeException {

  public AlreadyFollowUserException(String message) {
    super(message);
  }
}
