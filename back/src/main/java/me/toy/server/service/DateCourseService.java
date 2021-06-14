package me.toy.server.service;

import lombok.RequiredArgsConstructor;
import me.toy.server.cloud.S3Uploader;
import me.toy.server.dto.RegistDateCourseRequestDto;
import me.toy.server.dto.RegistDateCourseRequestDtoList;
import me.toy.server.entity.*;
import me.toy.server.exception.DateCourseNotFoundException;
import me.toy.server.exception.UserNotFoundException;
import me.toy.server.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DateCourseService {

    private final UserRepository userRepository;
    private final DateCourseRepository dateCourseRepository;
    private final LocationRepository locationRepository;
    private final LocationTagRepository locationTagRepository;
    private final LikeCourseRepository likeCourseRepository;
    private final TagRepository tagRepository;
    private final S3Uploader s3Uploader;
    public void regist(RegistDateCourseRequestDtoList requestDtoList, String title, String userEmail) {

        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
            new UserNotFoundException("그런 이메일을 가진 사용자는 없습니다.")
        );

        DateCourse dateCourse = new DateCourse(user,0L,title);
        dateCourseRepository.save(dateCourse);

        for(RegistDateCourseRequestDto requestDto: requestDtoList.getLocationList()){
            if(requestDto.getFile()==null){
                Location location = new Location(requestDto,"");
                location.setDateCourse(dateCourse);
                locationRepository.save(location);
                tagFindCircuit(requestDto, location);
                continue;
            }

            String fileSaveName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + ".jpg";
            Location location = new Location(requestDto,fileSaveName);
            location.setDateCourse(dateCourse);
            locationRepository.save(location);
            tagFindCircuit(requestDto, location);
            s3Uploader.upload(requestDto.getFile(),fileSaveName);
        }
    }

    private void tagFindCircuit(RegistDateCourseRequestDto requestDto, Location location) {
        for(String hashTag:requestDto.getHashTag()){
            Optional<Tag> opTag = tagRepository.findByTagName(hashTag);
            if(opTag.isPresent()){
                LocationTag locationTag = new LocationTag(location, opTag.get());
                locationTagRepository.save(locationTag);
                continue;
            }
            Tag tag = new Tag(hashTag);
            tagRepository.save(tag);
            LocationTag locationTag = new LocationTag(location, tag);
            locationTagRepository.save(locationTag);
        }
    }

    @Transactional
    public void plusOrMinusLike(String userEmail, Long dateCourseId) {

        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new UserNotFoundException("해당 이메일을 가진 사용자는 없습니다.")
        );
        List<LikeCourse> likeCourses = user.getLikeCourses();
        DateCourse dateCourse = dateCourseRepository.findById(dateCourseId).orElseThrow(() ->
                new DateCourseNotFoundException("찾으시는 데이트 코스는 없습니다."));

    }
}
