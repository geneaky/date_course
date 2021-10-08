package me.toy.server.service;


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
import me.toy.server.exception.s3.NotSupportedExtentionException;
import me.toy.server.utils.SupportedFileExtention;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service implements FileService {

  private final AmazonS3 amazonS3;
  @Value("${cloud.aws.s3.image.bucket}")
  private String BUCKET;
  private StringBuffer buffer = new StringBuffer();

  public String upload(MultipartFile file) {

    if (file == null) {
      return "";
    }

    if (!SupportedFileExtention.isSupported(
        FilenameUtils.getExtension(file.getOriginalFilename()))) {

      throw new NotSupportedExtentionException("지원되지 않은 파일 형식입니다.");
    }

    String saveFileName = createFileName();
    Path filePath = convert(file, saveFileName);
    amazonS3.putObject(BUCKET, saveFileName, filePath.toFile());

    try {
      Files.deleteIfExists(filePath);
    } catch (IOException exception) {
      log.warn("삭제되지 않은 이미지를 지워주세요");
    }

    return saveFileName;
  }

  @Override
  public void delete(String fileName) {

    if (fileName != null) {

      amazonS3.deleteObject(BUCKET, fileName);
    }
  }

  private String createFileName() {
    buffer.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
    buffer.append(".jpg");
    String saveFileName = buffer.toString();
    buffer.delete(0, buffer.length());
    return saveFileName;
  }

  private Path convert(MultipartFile file, String saveFileName) {

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
