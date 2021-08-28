package me.toy.server.utils.constants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseConstants {

  public static final ResponseEntity<String> USER_NOT_FOUND =
      new ResponseEntity<>("해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

  public static final ResponseEntity<String> DATE_COURSE_NOT_FOUND =
      new ResponseEntity<>("해당되는 데이트 코스가 없습니다.", HttpStatus.NOT_FOUND);

  public static final ResponseEntity<String> IMAGE_NOT_CONVERTED =
      new ResponseEntity<>("이미지 등록 실패", HttpStatus.SERVICE_UNAVAILABLE);

  public static final ResponseEntity<String> INVALID_REQUEST =
      new ResponseEntity<>("유효하지 않은 요청", HttpStatus.BAD_REQUEST);
}
