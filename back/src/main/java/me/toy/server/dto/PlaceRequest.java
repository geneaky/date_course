package me.toy.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlaceRequest {

    private String placeName;
    private String posX;
    private String posY;

}
