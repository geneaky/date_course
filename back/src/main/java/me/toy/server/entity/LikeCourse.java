package me.toy.server.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class LikeCourse {

    @Id
    @GeneratedValue
    @Column(name="likeCourse_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="datecourse_id")
    private  DateCourse dateCourse;

    public void setUser(User user){
        this.user = user;
        user.getLikeCourses().add(this);
    }

    public void setDateCourse(DateCourse dateCourse){
        this.dateCourse = dateCourse;
        dateCourse.getLikeCourses().add(this);
    }

}