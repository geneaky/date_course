package me.toy.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue
    @Column(name ="user_id")
    private Long id;
    private String username;
    private String userProfile;
    private String user_email;

    @OneToMany
    private List<DateCourse> dateCourses = new ArrayList<>();
}
