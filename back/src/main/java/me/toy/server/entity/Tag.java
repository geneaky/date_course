package me.toy.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue
    @Column(name = "tag_id")
    private Long id;
    private String tagName;

    @OneToMany(mappedBy = "tag")
    private List<DateCourseTag> courseTags = new ArrayList<>();
}
