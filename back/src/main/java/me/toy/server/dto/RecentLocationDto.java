package me.toy.server.dto;

import lombok.Data;
import me.toy.server.entity.Location;

import java.util.List;
import java.util.stream.Collectors;


@Data
public class RecentLocationDto {
    private String locationName;
    private String photoUrl;
    private String text;
    private float posx;
    private float posy;

    private List<String> tags;

    public RecentLocationDto(Location location) {
        this.locationName = location.getLocationName();
        this.photoUrl = location.getPhotoUrl();
        this.text = location.getText();
        this.posx = location.getPosx();
        this.posy = location.getPosy();
        this.tags = location.getLocationTags()
                .stream()
                .map(locationTag -> new String(locationTag.getTag().getTagName()))
                .collect(Collectors.toList());
    }
}
