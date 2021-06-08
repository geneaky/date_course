package me.toy.server.service;

import lombok.RequiredArgsConstructor;
import me.toy.server.cloud.S3Uploader;
import me.toy.server.dto.RegistDateCourseRequestDto;
import me.toy.server.dto.RegistDateCourseRequestDtoList;
import me.toy.server.entity.*;
import me.toy.server.repository.*;
import me.toy.server.security.jwt.JwtTokenProvider;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DateCourseService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final DateCourseRepository dateCourseRepository;
    private final LocationRepository locationRepository;
    private final LocationTagRepository locationTagRepository;
    private final TagRepository tagRepository;
    private final S3Uploader s3Uploader;
    public void regist(RegistDateCourseRequestDtoList requestDtoList, String title, HttpServletRequest request) {

        User user = userRepository.findById(jwtTokenProvider.getUserIdFromRequest(request)).get();

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
            s3Uploader.upload(requestDto.getFile(),fileSaveName);
            tagFindCircuit(requestDto, location);
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
}
