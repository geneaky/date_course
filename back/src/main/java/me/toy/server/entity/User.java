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
@Builder
public class User {

  @Id
  @GeneratedValue
  @Column(name = "USER_ID")
  private Long id;
  private String name;
  private String password;
  private String imageUrl;
  private String email;
  @Enumerated(EnumType.STRING)
  private AuthProvider provider;
  private String providerId;
  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<DateCourse> dateCourses = new ArrayList<>();
  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<UserDateCourseSave> userDateCoursSaves = new ArrayList<>();
  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<UserDateCourseLike> userDateCourseLikes = new ArrayList<>();
  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();

}
