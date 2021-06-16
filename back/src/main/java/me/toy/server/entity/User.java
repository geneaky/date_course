package me.toy.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import me.toy.server.security.oauth2.AuthProvider;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Column(name ="user_id")
    private Long id;
    private String name;
    @JsonIgnore
    private String password;
    private String imageUrl;
    private String email;
    private boolean emailVerified = false;
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    private String providerId;

    @OneToMany
    private List<DateCourse> dateCourses = new ArrayList<>();

    @OneToMany
    private List<SavedCourse> savedCourses = new ArrayList<>();

    @OneToMany
    private List<Like> likes = new ArrayList<>();

    @OneToMany
    private List<Comment> comments = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", email='" + email + '\'' +
                ", emailVerified=" + emailVerified +
                ", provider=" + provider +
                ", providerId='" + providerId + '\'' +
                ", dateCourses=" + dateCourses +
                '}';
    }
}
