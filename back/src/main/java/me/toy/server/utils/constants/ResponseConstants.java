package me.toy.server.utils.constants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseConstants {

  public static final ResponseEntity<String> USER_NOT_FOUND =
      new ResponseEntity<>("해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

  public static final ResponseEntity<String> COURSE_NOT_FOUND =
      new ResponseEntity<>("해당되는 데이트 코스가 없습니다.", HttpStatus.NOT_FOUND);

  public static final ResponseEntity<String> IMAGE_NOT_CONVERTED =
      new ResponseEntity<>("이미지 등록 실패", HttpStatus.SERVICE_UNAVAILABLE);

  public static final ResponseEntity<String> INVALID_REQUEST =
      new ResponseEntity<>("유효하지 않은 요청", HttpStatus.BAD_REQUEST);

  public static final ResponseEntity<String> DUPLICATED_EMAIL =
      new ResponseEntity<>("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT);

  public static final ResponseEntity<String> NO_REDIRECTURI_PARAM =
      new ResponseEntity<>("리다이렉트 파라미터가 포함되지 않은 요청입니다.", HttpStatus.BAD_REQUEST);

  public static final ResponseEntity<String> NOT_SUPPORTED_FILE_EXTENTION =
      new ResponseEntity<>("지원되지 않는 이미지 확장자 입니다.", HttpStatus.BAD_REQUEST);

  public static final ResponseEntity<String> ALREADY_LIKE_COURSE =
      new ResponseEntity<>("이미 좋아요 표시된 코스 입니다.", HttpStatus.BAD_REQUEST);

  public static final ResponseEntity<String> ALREADY_UNLIKE_COURSE =
      new ResponseEntity<>("아직 좋아요를 누르지 않은 코스 입니다.", HttpStatus.BAD_REQUEST);

  public static final ResponseEntity<String> ALREADY_FOLLOW_USER =
      new ResponseEntity<>("이미 팔로우한 유저 입니다.", HttpStatus.BAD_REQUEST);

  public static final ResponseEntity<String> ALREADY_UNFOLLOW_USER =
      new ResponseEntity<>("팔로우 되어 있지 않은 유저 입니다.", HttpStatus.BAD_REQUEST);
}
