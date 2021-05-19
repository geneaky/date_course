package me.toy.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CourseRegisterRequestDto {

    private List<LocationRequest> course;

}
