package me.toy.server.exception.user;

public class AlreadyUnfollowUserException extends RuntimeException {

  public AlreadyUnfollowUserException(String message) {
    super(message);
  }
}
