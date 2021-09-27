package me.toy.server.exception;

import static me.toy.server.utils.constants.ResponseConstants.ALREADY_FOLLOW_USER;
import static me.toy.server.utils.constants.ResponseConstants.ALREADY_LIKE_COURSE;
import static me.toy.server.utils.constants.ResponseConstants.ALREADY_UNFOLLOW_USER;
import static me.toy.server.utils.constants.ResponseConstants.ALREADY_UNLIKE_COURSE;
import static me.toy.server.utils.constants.ResponseConstants.COURSE_NOT_FOUND;
import static me.toy.server.utils.constants.ResponseConstants.DUPLICATED_EMAIL;
import static me.toy.server.utils.constants.ResponseConstants.IMAGE_NOT_CONVERTED;
import static me.toy.server.utils.constants.ResponseConstants.INVALID_REQUEST;
import static me.toy.server.utils.constants.ResponseConstants.NOT_SUPPORTED_FILE_EXTENTION;
import static me.toy.server.utils.constants.ResponseConstants.NO_REDIRECTURI_PARAM;
import static me.toy.server.utils.constants.ResponseConstants.USER_NOT_FOUND;

import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import me.toy.server.exception.course.AlreadyLikeCourseException;
import me.toy.server.exception.course.AlreadyUnlikeCourseException;
import me.toy.server.exception.course.CourseNotFoundException;
import me.toy.server.exception.s3.ImageConvertFailedException;
import me.toy.server.exception.s3.NotSupportedExtentionException;
import me.toy.server.exception.security.NoRedirectUriRequestException;
import me.toy.server.exception.user.AlreadyFollowUserException;
import me.toy.server.exception.user.AlreadyUnfollowUserException;
import me.toy.server.exception.user.EmailDuplicationException;
import me.toy.server.exception.user.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public final ResponseEntity<String> hadleValidationException(
      ConstraintViolationException exception) {

    log.info(exception.getMessage(), exception.getCause());
    return INVALID_REQUEST;
  }

  @ExceptionHandler(NoRedirectUriRequestException.class)
  public final ResponseEntity<String> handleNoRedirectUriRequestException(
      NoRedirectUriRequestException exception) {

    log.info(exception.getMessage(), exception.getCause());
    return NO_REDIRECTURI_PARAM;
  }

  @ExceptionHandler(EmailDuplicationException.class)
  public final ResponseEntity<String> handleEmailDuplicationException(
      EmailDuplicationException exception) {

    log.info(exception.getMessage(), exception.getCause());
    return DUPLICATED_EMAIL;
  }

  @ExceptionHandler(UserNotFoundException.class)
  public final ResponseEntity<String> handleUserNotFoundException(UserNotFoundException exception) {

    log.info(exception.getMessage(), exception.getCause());
    return USER_NOT_FOUND;
  }

  @ExceptionHandler(CourseNotFoundException.class)
  public final ResponseEntity<String> handleDateCourseNotFoundException(
      CourseNotFoundException exception) {

    log.info(exception.getMessage(), exception.getCause());
    return COURSE_NOT_FOUND;
  }

  @ExceptionHandler(ImageConvertFailedException.class)
  public final ResponseEntity<String> handleImageConvertFailedException(
      ImageConvertFailedException exception) {

    log.info(exception.getMessage(), exception.getCause());
    return IMAGE_NOT_CONVERTED;
  }

  @ExceptionHandler(NotSupportedExtentionException.class)
  public final ResponseEntity<String> hadleNotSupportedExtensionException(
      NotSupportedExtentionException exception) {

    log.info(exception.getMessage(), exception.getCause());
    return NOT_SUPPORTED_FILE_EXTENTION;
  }

  @ExceptionHandler(AlreadyLikeCourseException.class)
  public final ResponseEntity<String> handleAlreadyLikeCourseException(
      AlreadyLikeCourseException exception) {

    log.info(exception.getMessage(), exception.getCause());
    return ALREADY_LIKE_COURSE;
  }

  @ExceptionHandler(AlreadyUnlikeCourseException.class)
  public final ResponseEntity<String> handleAlreadyUnlikeCourseException(
      AlreadyUnlikeCourseException exception) {

    log.info(exception.getMessage(), exception.getCause());
    return ALREADY_UNLIKE_COURSE;
  }

  @ExceptionHandler(AlreadyFollowUserException.class)
  public final ResponseEntity<String> handleAlreadyFollowUserException(
      AlreadyFollowUserException exception) {

    log.info(exception.getMessage(), exception.getCause());
    return ALREADY_FOLLOW_USER;
  }

  @ExceptionHandler(AlreadyUnfollowUserException.class)
  public final ResponseEntity<String> handleAlreadyUnfollowUserException(
      AlreadyUnfollowUserException exception) {

    log.info(exception.getMessage(), exception.getCause());
    return ALREADY_UNFOLLOW_USER;
  }
}
