package me.toy.server.utils;

public enum SupportedFileExtention {

  jpg, png;

  public static boolean isSupported(String fileName) {
    for (SupportedFileExtention extension : SupportedFileExtention.values()) {
      if (extension.toString().equals(fileName)) {
        return true;
      }
    }

    return false;
  }
}
