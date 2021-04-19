package me.toy.server.dto;

import lombok.Data;
import me.toy.server.entity.Location;

@Data
public class RecentLocationDto {
    private String locationName;
    private String photoUrl;
    private String text;
    private float posx;
    private float posy;


    public RecentLocationDto(Location location) {
        this.locationName = location.getLocationName();
        this.photoUrl = location.getPhotoUrl();
        this.text = location.getText();
        this.posx = location.getPosx();
        this.posy = location.getPosy();
    }
}
