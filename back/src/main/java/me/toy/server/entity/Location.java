package me.toy.server.entity;

import lombok.*;
import me.toy.server.dto.RegistDateCourseRequestDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    @Id
    @GeneratedValue
    @Column(name = "location_id")
    private Long id;
    private String locationName;
    private String text;

    private String photoUrl;
    private float posx;
    private float posy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="datecourse_id")
    private DateCourse dateCourse;

    @OneToMany(mappedBy = "location")
    private List<LocationTag> LocationTags = new ArrayList<>();

    public Location(RegistDateCourseRequestDto requestDto,String photoUrl){
        this.locationName = requestDto.getPlaceName();
        this.text = requestDto.getText();
        this.posx = requestDto.getPosX();
        this.posy = requestDto.getPosY();
        this.photoUrl = photoUrl;
    }

    public void setDateCourse(DateCourse dateCourse){
        this.dateCourse = dateCourse;
        dateCourse.getLocations().add(this);
    }
}
