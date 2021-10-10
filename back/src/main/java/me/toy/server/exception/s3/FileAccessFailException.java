package me.toy.server.exception.s3;

public class FileAccessFailException extends RuntimeException {

  public FileAccessFailException(String message) {
    super(message);
  }
}
