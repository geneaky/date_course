package me.toy.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import me.toy.server.exception.s3.ImageConvertFailedException;
import me.toy.server.exception.s3.NotSupportedExtentionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

  @Mock
  AmazonS3 amazonS3;
  @InjectMocks
  S3Service s3Service;

  @Test
  @DisplayName("file 인자가 null인 경우 빈 리터럴을 반환한다.")
  public void uploadNullFileTest() throws Exception {

    MultipartFile file = null;

    String upload = s3Service.upload(file);

    assertThat(upload).isEqualTo("");
    verify(amazonS3, never()).putObject(anyString(), anyString(), (File) any());
  }

  @Test
  @DisplayName("지원되지 않는 형식의 file을 upload할 경우 예외를 던진다.")
  public void uploadNotSupportedFileTest() throws Exception {

    MultipartFile file = mock(MultipartFile.class);

    when(file.getName()).thenReturn("test.txt");

    Assertions.assertThrows(NotSupportedExtentionException.class, () -> {
      s3Service.upload(file);
    });
    verify(amazonS3, never()).putObject(anyString(), anyString(), (File) any());
  }

  @Test
  @DisplayName("null이 아닌 지원되는 형식의 파일을 upload에 성공한다.")
  public void uploadNormalyTest() throws Exception {

    MultipartFile file = mock(MultipartFile.class);
    InputStream inputStream = mock(InputStream.class);

    when(file.getName()).thenReturn("test.jpg");
    when(file.getInputStream()).thenReturn(inputStream);

    String saveFileName = s3Service.upload(file);

    assertThat(saveFileName).isExactlyInstanceOf(String.class);
    verify(amazonS3).putObject(any(), any(), (File) any());
  }

  @Test
  @DisplayName("이미지 변환에 실패하면 Exception을 던진다.")
  public void imageConvertFailTest() throws Exception {

    MultipartFile file = mock(MultipartFile.class);
    InputStream inputStream = mock(InputStream.class);

    when(file.getName()).thenReturn("test.png");
    when(file.getInputStream()).thenReturn(inputStream);

    try (MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {

      when(Files.copy((InputStream) any(), any(), any())).thenThrow(IOException.class);
      Assertions.assertThrows(ImageConvertFailedException.class, () -> {
        s3Service.upload(file);
      });
    }
  }
}