package me.toy.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;
    private String  commentContents;
    private long thumbUp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datecourse_id")
    private DateCourse dateCourse;
}
