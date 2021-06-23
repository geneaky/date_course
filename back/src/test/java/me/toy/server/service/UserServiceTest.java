package me.toy.server.service;

import me.toy.server.dto.RecentDateCourseDto;
import me.toy.server.dto.SavedDateCourseDto;
import me.toy.server.dto.UserDto;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.Like;
import me.toy.server.entity.SavedCourse;
import me.toy.server.entity.User;
import me.toy.server.exception.UserNotFoundException;
import me.toy.server.repository.DateCourseRepository;
import me.toy.server.repository.SavedCourseRepository;
import me.toy.server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    DateCourseRepository dateCourseRepository;

    @Mock
    SavedCourseRepository savedCourseRepository;

    @InjectMocks
    UserService userService;


    @Test
    public void 사용자이메일_빈칸_찾기_실패_예외던지기() throws Exception
    {
        //given
        //when
        assertThrows(UserNotFoundException.class,()->{
            userService.findUser("");
        });
        //then
    }

    @Test
    public void 사용자_필수정보_응답() throws Exception
    {
        //given
        User user = User.builder()
                .id(1L)
                .email("test@naver.com")
                .name("user01")
                .password("nopassword")
                .build();
        Optional<User> oUser = Optional.of(user);
        userRepository.save(user);
        when(userRepository.findByEmail("test@naver.com"))
                .thenReturn(oUser);
        //when
        UserDto userDto = userService.findUser("test@naver.com");
        //then
        assertEquals(userDto.getEmail(),"test@naver.com");
        verify(userRepository,atLeast(1)).findByEmail(any());
    }

    @Test
    public void 사용자_좋아요_코스_리스트_반환() throws Exception
    {
        //given
        String userEmail = "test@naver.com";
        User user = new User();
        user.setEmail(userEmail);
        DateCourse dateCourse = new DateCourse(user,1L,"test");
        List<Like> likes = new ArrayList<>();
        Like like = new Like(user,dateCourse);
        likes.add(like);
        user.setLikes(likes);
        userRepository.save(user);
        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(user));
        //when
        List<Long> testLikedCourse = user.getLikes()
                .stream()
                .map(testLike -> testLike.getDateCourse().getId())
                .collect(Collectors.toList());
        List<Long> likedCourse = userService.findLikedCourse(userEmail);
        //then
        assertEquals(testLikedCourse,likedCourse);
        verify(userRepository,atLeast(1)).findByEmail(any());
    }

    @Test
    public void 사용자_좋아요_코스_없을때_빈리스트_반환() throws Exception
    {
        //given
        String userEmail = "test@naver.com";
        User user = new User();
        user.setEmail(userEmail);
        List<Like> likes = new ArrayList<>();
        user.setLikes(likes);
        userRepository.save(user);
        //when
        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(user));
        List<Long> likedCourse = userService.findLikedCourse(userEmail);
        //then
        assertEquals(likes,likedCourse);
        verify(userRepository,atLeast(1)).findByEmail(any());
    }

    @Test
    public void 사용자_저장할_코스_등록() throws Exception
    {
        //given
        String userEmail = "test@naver.com";
        User user = new User();
        user.setEmail(userEmail);
        DateCourse dateCourse = new DateCourse(user,0L,"test");
        SavedCourse savedCourse = new SavedCourse(user,dateCourse);
        //when
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(dateCourseRepository.findById(dateCourse.getId())).thenReturn(Optional.of(dateCourse));
        Long savedCourseId = userService.registSavedCourse(dateCourse.getId(), userEmail);
        //then
        assertEquals(savedCourseId,savedCourse.getId());
        verify(userRepository,atLeast(1)).findByEmail(any());
        verify(dateCourseRepository,atLeast(1)).findById(any());
    }

    @Test
    public void 사용자_저장한_코스ID_리스트_반환() throws Exception
    {
        //given
        String userEmail = "test@naver.com";
        User user = new User();
        user.setEmail(userEmail);
        DateCourse dateCourse = new DateCourse(user,0L,"test");
        SavedCourse savedCourse = new SavedCourse(user,dateCourse);
        List<SavedCourse> savedCourseList = new ArrayList<>();
        savedCourseList.add(savedCourse);
        user.setSavedCourses(savedCourseList);
        //when
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        List<Long> resultSavedCourse = userService.findSavedCourse(userEmail);
        List<Long> testSavedCourseList = user.getSavedCourses()
                .stream()
                .map(testSavedCourse -> testSavedCourse.getDateCourse().getId())
                .collect(Collectors.toList());
        //then
        assertEquals(resultSavedCourse,testSavedCourseList);
        verify(userRepository,atLeast(1)).findByEmail(any());
    }

    @Test
    public void 사용자_저장한_코스_없을때_빈리스트_반환() throws Exception
    {
        //given
        String userEmail = "test@naver.com";
        User user = new User();
        user.setEmail(userEmail);
        List<Long> testSavedCourse = new ArrayList<>();
        //when
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        List<Long> savedCourse = userService.findSavedCourse(userEmail);
        //then
        assertEquals(savedCourse,testSavedCourse);
        verify(userRepository,atLeast(1)).findByEmail(any());
    }

    @Test
    public void 사용자_저장한_코스_삭제() throws Exception
    {
        //given
        String userEmail = "test@naver.com";
        User user = new User();
        user.setEmail(userEmail);
        DateCourse dateCourse = new DateCourse(user,0L,"test");
        SavedCourse savedCourse = new SavedCourse(user,dateCourse);
        List<SavedCourse> savedCourseList = new ArrayList<>();
        savedCourseList.add(savedCourse);
        user.setSavedCourses(savedCourseList);
        //when
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(dateCourseRepository.findById(dateCourse.getId())).thenReturn(Optional.of(dateCourse));
        userService.deleteSavedCourse(dateCourse.getId(),userEmail);
        //then
        assertEquals(user.getSavedCourses().size(),0);
        verify(userRepository,atLeast(1)).findByEmail(any());
        verify(dateCourseRepository,atLeast(1)).findById(any());
    }

    @Test
    public void 사용자_작성한_코스_DtoList_반환() throws Exception
    {
        //given
        String userEmail = "test@naver.com";
        User user = new User();
        user.setEmail(userEmail);
        DateCourse dateCourse1 = new DateCourse(user,0L,"test1");
        DateCourse dateCourse2 = new DateCourse(user,0L,"test2");
        DateCourse dateCourse3 = new DateCourse(user,0L,"test3");
        List<DateCourse> dateCourseList = new ArrayList<>();
        dateCourseList.add(dateCourse1);
        dateCourseList.add(dateCourse2);
        dateCourseList.add(dateCourse3);
        user.setDateCourses(dateCourseList);
        List<RecentDateCourseDto> testMyCourse = new ArrayList<>();
        testMyCourse.add(new RecentDateCourseDto(dateCourse1));
        testMyCourse.add(new RecentDateCourseDto(dateCourse2));
        testMyCourse.add(new RecentDateCourseDto(dateCourse3));
        //when
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(dateCourseRepository.findAllDateCourseByUserId(user.getId())).thenReturn(testMyCourse);
        List<RecentDateCourseDto> myCourse = userService.findMyCourse(userEmail);
        //then
        assertEquals(myCourse.size(),3);
    }

    @Test
    public void 사용자_저장한_코스_리스트_반환() throws Exception
    {
        //given
        String userEmail = "test@naver.com";
        User user = new User();
        user.setEmail(userEmail);
        DateCourse dateCourse = new DateCourse(user,0L,"test");
        SavedCourse savedCourse = new SavedCourse(user,dateCourse);
        List<SavedCourse> savedCourseList = new ArrayList<>();
        savedCourseList.add(savedCourse);
        user.setSavedCourses(savedCourseList);
        List<SavedDateCourseDto> testSavedCourse = new ArrayList<>();
        testSavedCourse.add(new SavedDateCourseDto(savedCourse));
        //when
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(dateCourseRepository.findAllSavedCourseByUserId(user.getId())).thenReturn(testSavedCourse);
        List<SavedDateCourseDto> savedCourseListResult = userService.findSavedCourseList(userEmail);
        //then
        assertEquals(savedCourseListResult.size(),1);
    }
}