package me.toy.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserRequest {

    private List<MultipartFile> photos;
    private String text;

    public UserRequest(List<MultipartFile> photos, String text) {
        this.photos = photos;
        this.text = text;
    }
}
