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
  @Column(name = "LOCATIONTAG_ID")
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "LOCATION_ID")
  private Location location;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TAG_ID")
  private Tag tag;

  public LocationTag(Location location, Tag tag) {

    this.location = location;
    this.tag = tag;
    location.getLocationTags().add(this);
    tag.getLocationTags().add(this);
  }
}
