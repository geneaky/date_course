package me.toy.server.service;

import me.toy.server.cloud.S3Uploader;
import me.toy.server.dto.RecentDateCourseDto;
import me.toy.server.dto.RegistDateCourseRequestDto;
import me.toy.server.dto.RegistDateCourseRequestDtoList;
import me.toy.server.entity.DateCourse;
import me.toy.server.entity.User;
import me.toy.server.repository.DateCourseRepository;
import me.toy.server.repository.LocationRepository;
import me.toy.server.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DateCourseServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    DateCourseRepository dateCourseRepository;
    @Mock
    LocationRepository locationRepository;
    @Mock
    S3Uploader s3Uploader;
    @Mock
    RegistDateCourseRequestDtoList requestDtoList;
    @Mock
    RegistDateCourseRequestDto requestDto;

    @InjectMocks
    DateCourseService dateCourseService;

    @Test
    @Disabled
    public void 데이트코스_등록() throws Exception
    {
        //given
        String userEmail = "test@naver.com";
        String title = "testTitle";
        User user = new User();
        user.setEmail(userEmail);
        MockMultipartFile multipartFile = new MockMultipartFile("file","test","MultipartFile","test".getBytes());
        //when
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(requestDto.getFile()).thenReturn(multipartFile);
        dateCourseService.regist(requestDtoList,title,userEmail);
        List<RecentDateCourseDto> allDateCourseByUserId = dateCourseRepository.findAllDateCourseByUserId(user.getId());
        //then
        assertEquals(allDateCourseByUserId.size(),1);
    }
}