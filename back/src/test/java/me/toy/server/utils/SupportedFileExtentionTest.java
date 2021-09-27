package me.toy.server.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SupportedFileExtentionTest {

  @Test
  @DisplayName("지원되는 확장자에 대해서는 true를 반환한다.")
  public void testIsSupported() throws Exception {

    String fileExtension1 = "jpg";
    String fileExtention2 = "png";

    assertThat(SupportedFileExtention.isSupported(fileExtension1)).isTrue();
    assertThat(SupportedFileExtention.isSupported(fileExtention2)).isTrue();
  }

  @Test
  @DisplayName("지원되지 않는 확장자에 대해서는 false를 반환한다.")
  public void testIsNotSupported() throws Exception {

    String fileExtention1 = "txt";
    String fileExtention2 = "gif";

    assertThat(SupportedFileExtention.isSupported(fileExtention1)).isFalse();
    assertThat(SupportedFileExtention.isSupported(fileExtention2)).isFalse();
  }
}