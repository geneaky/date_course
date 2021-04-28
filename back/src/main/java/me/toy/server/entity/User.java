package me.toy.server.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue
    @Column(name ="user_id")
    private Long id;
    private String username;
    private String password;
    private String userProfile;
    private String email;
    private String role;
    private String provider;
    private String providerId;

    @OneToMany
    private List<DateCourse> dateCourses = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userProfile='" + userProfile + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
