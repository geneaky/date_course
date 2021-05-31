package me.toy.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
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
    private String dateCourseTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany
    private List<Location> locations = new ArrayList<>();

    @OneToMany
    private List<Comment> comments = new ArrayList<>();

    public DateCourse(User user,Long thumbUp,String dateCourseTitle) {
        setUser(user);
        this.thumbUp = thumbUp;
        this.dateCourseTitle = dateCourseTitle;
    }

    public void setUser(User user){
        this.user = user;
        user.getDateCourses().add(this);
    }

}
