package me.toy.server.service;

import lombok.RequiredArgsConstructor;
import me.toy.server.dto.UserDto;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.SavedCourse;
import me.toy.server.entity.User;
import me.toy.server.exception.DateCourseNotFoundException;
import me.toy.server.exception.UserNotFoundException;
import me.toy.server.repository.DateCourseRepository;
import me.toy.server.repository.SavedCourseRepository;
import me.toy.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DateCourseRepository dateCourseRepository;
    private final SavedCourseRepository savedCourseRepository;

    public UserDto findUser(String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
        );
        return new UserDto(user);
    }

    public List<Long> findLikedCourse(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
        );
        List<Long> result = user.getLikes()
                .stream()
                .map(like -> like.getDateCourse().getId())
                .collect(Collectors.toList());
        return result;
    }

    public void registUserCourse(Long courseId, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
        );
        DateCourse dateCourse = dateCourseRepository.findById(courseId).orElseThrow(() ->
                new DateCourseNotFoundException("찾으시는 데이트 코스는 없습니다."));
        SavedCourse savedCourse = new SavedCourse(user,dateCourse);
        savedCourseRepository.save(savedCourse);
    }

    public List<Long> findSavedCourse(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
        );

        List<Long> result = user.getSavedCourses()
                .stream()
                .map(savedCourse -> savedCourse.getDateCourse().getId())
                .collect(Collectors.toList());

        return result;
    }

    public void deleteUserCourse(Long courseId, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
        );
        DateCourse dateCourse = dateCourseRepository.findById(courseId).orElseThrow(() ->
                new DateCourseNotFoundException("찾으시는 데이트 코스는 없습니다."));
        for (SavedCourse savedCourse : user.getSavedCourses()) {
            if(savedCourse.getDateCourse().getId()==courseId){
                dateCourse.getSavedCourses().remove(savedCourse);
                user.getSavedCourses().remove(savedCourse);
                savedCourseRepository.delete(savedCourse);
                return;
            }
        }
    }
}
