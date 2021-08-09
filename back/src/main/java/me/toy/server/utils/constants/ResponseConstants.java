package me.toy.server.utils.constants;

import me.toy.server.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseConstants {

  public static ResponseEntity<UserNotFoundException> USER_NOT_FOUND =
      new ResponseEntity<>(HttpStatus.BAD_REQUEST);

}
