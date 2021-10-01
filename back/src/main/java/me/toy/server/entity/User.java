package me.toy.server.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.toy.server.security.oauth2.OAuth2Provider;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "USER_ID")
  private Long id;
  @Column(unique = true)
  private String email;
  private String password;
  private String name;
  private String imageUrl;
  @Enumerated(EnumType.STRING)
  private OAuth2Provider provider;
  private String providerId;

  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<Course> course = new ArrayList<>();
  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<UserCourseSave> userCourseSaves = new ArrayList<>();
  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<UserCourseLike> userCourseLikes = new ArrayList<>();
  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();
  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<Follow> follows = new ArrayList<>();

  public void updateUserName(String userName) {
    this.name = userName;
  }

  public void updateImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
