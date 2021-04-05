package me.toy.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    @Id
    @GeneratedValue
    @Column(name = "location_id")
    private Long id;
    private String locationName;
    private String photoUrl;
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="datecourse_id")
    private DateCourse dateCourse;
}
