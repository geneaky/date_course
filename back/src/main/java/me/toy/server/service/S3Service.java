package me.toy.server.service;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.toy.server.exception.s3.AwsS3FileUploadFailException;
import me.toy.server.exception.s3.FileAccessFailException;
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

    try (InputStream inputStream = file.getInputStream()) {
      amazonS3.putObject(BUCKET, saveFileName, inputStream, getFileMetadata(file));
    } catch (AmazonServiceException e) {
      throw new AwsS3FileUploadFailException("storage에 파일 업로드 실패 했습니다.");
    } catch (IOException e) {
      throw new FileAccessFailException("file에 접근할 수 없습니다.");
    }

    return saveFileName;
  }

  private ObjectMetadata getFileMetadata(MultipartFile file) {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(file.getSize());
    objectMetadata.setContentType(file.getContentType());

    return objectMetadata;
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
}
