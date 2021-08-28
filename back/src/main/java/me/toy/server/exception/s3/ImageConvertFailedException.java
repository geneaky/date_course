package me.toy.server.exception.s3;

public class ImageConvertFailedException extends RuntimeException {

  public ImageConvertFailedException(String message) {
    super(message);
  }
}
