package me.toy.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String photoUrl="";
    private String text;
    private String posx;
    private String posy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="datecourse_id")
    private DateCourse dateCourse;

    @OneToMany(mappedBy = "location")
    private List<LocationTag> LocationTags = new ArrayList<>();

    public Location(String locationName,String text, String posx, String posy) {
        this.locationName = locationName;
        this.text = text;
        this.posx = posx;
        this.posy = posy;
    }

    public void setDateCourse(DateCourse dateCourse){
        this.dateCourse = dateCourse;
        dateCourse.getLocations().add(this);
    }

    public void setPhotoUrls(List<String> photoUrls) {
        for(String photoUrl:photoUrls){
            this.photoUrl += (photoUrl + ":");
        }
    }
}
