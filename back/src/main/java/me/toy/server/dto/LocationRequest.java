package me.toy.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LocationRequest {

    public PlaceRequest place;
    public UserRequest user;
}
