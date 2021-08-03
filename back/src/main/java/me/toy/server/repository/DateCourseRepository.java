package me.toy.server.repository;

import me.toy.server.dto.DateCourseResponseDto.CurrentLocationDateCourseDto;
import me.toy.server.dto.DateCourseResponseDto.RecentDateCourseDto;
import me.toy.server.dto.DateCourseResponseDto.LikeOrderDateCourseDto;
import me.toy.server.dto.UserResponseDto.SavedDateCourseDto;
import me.toy.server.entity.DateCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DateCourseRepository extends JpaRepository<DateCourse, Long> {

  @Query("select distinct d from DateCourse d join fetch d.locations order by d.id desc")
  List<RecentDateCourseDto> findRecentDateCourse();

  @Query("select d from DateCourse d left join fetch d.locations order by d.userDateCourseLikes.size desc")
  List<LikeOrderDateCourseDto> findLikeOrderDateCourse();

  @Query("select d from DateCourse d left join fetch d.locations l " +
      "where :posX-0.001 < l.posx and l.posx < :posX+0.001 and :posY-0.001 < l.posy and l.posy < :posY+0.001 ")
  List<CurrentLocationDateCourseDto> findCurrentLocationDateCourse(float posX,
      float posY);

  @Modifying
  @Query("select d from DateCourse d where d.user.id = :userId")
  List<RecentDateCourseDto> findAllDateCourseByUserId(@Param("userId") Long userId);

  @Modifying
  @Query("select s from UserDateCourseSave s where s.user.id = :userId ")
  List<SavedDateCourseDto> findAllSavedCourseByUserId(
      @Param("userId") Long userId);//userdatecoursesaverepository로 변경해야함
}
