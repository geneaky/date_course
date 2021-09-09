package me.toy.server.exception;

import static me.toy.server.utils.constants.ResponseConstants.DATE_COURSE_NOT_FOUND;
import static me.toy.server.utils.constants.ResponseConstants.IMAGE_NOT_CONVERTED;
import static me.toy.server.utils.constants.ResponseConstants.INVALID_REQUEST;
import static me.toy.server.utils.constants.ResponseConstants.USER_NOT_FOUND;

import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import me.toy.server.exception.datecourse.DateCourseNotFoundException;
import me.toy.server.exception.s3.ImageConvertFailedException;
import me.toy.server.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public final ResponseEntity<String> hadleValidationException(
      ConstraintViolationException exception) {

    log.info(exception.getMessage());
    return INVALID_REQUEST;
  }

  @ExceptionHandler(UserNotFoundException.class)
  public final ResponseEntity<String> handleUserNotFoundException(UserNotFoundException exception) {

    log.info(exception.getMessage());
    return USER_NOT_FOUND;
  }

  @ExceptionHandler(DateCourseNotFoundException.class)
  public final ResponseEntity<String> handleDateCourseNotFoundException(
      DateCourseNotFoundException exception) {

    log.info(exception.getMessage());
    return DATE_COURSE_NOT_FOUND;
  }

  @ExceptionHandler(ImageConvertFailedException.class)
  public final ResponseEntity<String> handleImageConvertFailedException(
      ImageConvertFailedException exception) {

    log.info(exception.getMessage());
    return IMAGE_NOT_CONVERTED;
  }


}