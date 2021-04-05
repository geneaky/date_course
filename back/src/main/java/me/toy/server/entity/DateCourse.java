package me.toy.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class DateCourse {

    @Id
    @GeneratedValue
    @Column(name = "datecourse_id")
    private Long id;
    private Long thumbUp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany
    private List<Location> locations = new ArrayList<>();

    @OneToMany
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "dateCourse")
    private List<DateCourseTag> courseTags = new ArrayList<>();
}
