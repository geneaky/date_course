package me.toy.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CourseRequest {

    private List<LocationRequest> course;

    public CourseRequest(List<LocationRequest> course) {
        this.course = course;
    }
}
