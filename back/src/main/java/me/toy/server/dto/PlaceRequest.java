package me.toy.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlaceRequest {

    private String placeName;
    private String posX;
    private String posY;

    public PlaceRequest(String placeName, String posX, String posY) {
        this.placeName = placeName;
        this.posX = posX;
        this.posY = posY;
    }
}
