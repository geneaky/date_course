package me.toy.server.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  String upload(MultipartFile multipartFile);

  void delete(String fileName);
}
