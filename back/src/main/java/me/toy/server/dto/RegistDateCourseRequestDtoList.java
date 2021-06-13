package me.toy.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistDateCourseRequestDtoList {

    private ArrayList<RegistDateCourseRequestDto> locationList;
}
