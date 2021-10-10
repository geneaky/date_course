package me.toy.server.exception.s3;

public class AwsS3FileUploadFailException extends RuntimeException {

  public AwsS3FileUploadFailException(String message) {
    super(message);
  }
}
