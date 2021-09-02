package me.toy.server.cloud;


import com.amazonaws.services.s3.AmazonS3;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.toy.server.exception.s3.ImageConvertFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

  private final AmazonS3 amazonS3;
  @Value("${cloud.aws.s3.image.bucket}")
  private String BUCKET;
  private StringBuffer buffer = new StringBuffer();

  //io 작업 개선 및 aws s3 error 핸들링, 로깅
  public String upload(MultipartFile file) {

    if (file == null) {
      return "";
    }
    buffer.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
    buffer.append(".jpg");
    String saveFileName = buffer.toString();
    Path filePath = convert(file, saveFileName);

    amazonS3.putObject(BUCKET, saveFileName, filePath.toFile());
    try {
      Files.deleteIfExists(filePath);
    } catch (IOException exception) {
      log.warn("삭제되지 않은 이미지를 지워주세요");
    }

    return saveFileName;
  }

  public Path convert(MultipartFile file, String saveFileName) {

    Path filePath = Paths.get(saveFileName);
    try {
      Files.createFile(filePath);
      Files.copy(file.getInputStream(), filePath,
          StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException exception) {
      throw new ImageConvertFailedException("이미지 변환 실패");
    }

    return filePath;
  }
}
