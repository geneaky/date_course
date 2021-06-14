package me.toy.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SavedCourse {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private Long savedCourseId;

    public void setSavedCourse(User user,Long dateCourseId){
        this.user = user;
        this.savedCourseId = dateCourseId;
        user.getSavedCourses().add(this);
    }
}
