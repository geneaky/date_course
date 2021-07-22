package me.toy.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationTag {

  @Id
  @GeneratedValue
  @Column(name = "locationtag_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "location_id")
  private Location location;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tag_id")
  private Tag tag;

  public LocationTag(Location location, Tag tag) {
    this.location = location;
    this.tag = tag;
    location.getLocationTags().add(this);
    tag.getLocationTags().add(this);
  }
}
