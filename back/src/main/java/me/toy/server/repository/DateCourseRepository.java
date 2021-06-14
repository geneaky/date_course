package me.toy.server.repository;

import me.toy.server.dto.CurrentLocationDateCourseDto;
import me.toy.server.dto.RecentDateCourseDto;
import me.toy.server.dto.ThumbUpDateCourseDto;
import me.toy.server.entity.DateCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DateCourseRepository extends JpaRepository <DateCourse,Long> {

    @Query("select distinct d from DateCourse d join fetch d.locations order by d.id desc")
    List<RecentDateCourseDto> findRecentDatecourse();

    @Query("select d from DateCourse d left join fetch d.locations order by d.thumbUp desc")
    List<ThumbUpDateCourseDto> findThumbUpDatecourse();

    @Query("select d from DateCourse d join fetch d.locations l " +
            "where :posX-0.001 < l.posx and l.posx < :posX+0.001 and :posY-0.001 < l.posy and l.posy < :posY+0.001 ")
    List<CurrentLocationDateCourseDto> findCurrentLocationDatecourse(float posX,float posY);


    @Modifying
    @Query("update DateCourse d set d.thumbUp = d.thumbUp - 1 where d.id = :dateCourseId")
    void minusThumbUp(@Param("dateCourseId") Long dateCourseId);

    @Modifying
    @Query("update DateCourse d set d.thumbUp = d.thumbUp + 1 where d.id = :dateCourseId")
    void plusThumbUp(@Param("dateCourseId") Long dateCourseId);
}
