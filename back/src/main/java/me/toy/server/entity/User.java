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
public class User extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "USER_ID")
  private Long id;
  private String email;
  private String password;
  private String name;

  private String imageUrl;

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
  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<UserFollow> userFollows = new ArrayList<>();

}
