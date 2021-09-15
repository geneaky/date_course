package me.toy.server.exception.security;

public class NoRedirectUriRequestException extends RuntimeException {

  public NoRedirectUriRequestException(String message) {
    super(message);
  }
}
