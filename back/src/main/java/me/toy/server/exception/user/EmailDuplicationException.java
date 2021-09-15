package me.toy.server.exception.user;

public class EmailDuplicationException extends RuntimeException {

  public EmailDuplicationException(String message) {
    super(message);
  }
}
