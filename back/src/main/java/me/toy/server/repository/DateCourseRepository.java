package me.toy.server.repository;

import me.toy.server.dto.CurrentLocationDateCourseDto;
import me.toy.server.dto.RecentDateCourseDto;
import me.toy.server.dto.ThumbUpDateCourseDto;
import me.toy.server.entity.DateCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DateCourseRepository extends JpaRepository <DateCourse,Long> {

    @Query("select d from DateCourse d left join fetch d.locations order by d.id")
    public List<RecentDateCourseDto> findRecentDatecourse();

    @Query("select d from DateCourse d left join fetch d.locations order by d.thumbUp desc")
    List<ThumbUpDateCourseDto> findThumbUpDatecourse();

    @Query("select d from DateCourse d join fetch d.locations l " +
            "where :posX-10 < l.posx and l.posx < :posX+10 and :posY-10 < l.posy and l.posy < :posY+10 ")
    List<CurrentLocationDateCourseDto> findCurrentLocationDatecourse(float posX,float posY);
}