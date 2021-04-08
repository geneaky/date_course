package me.toy.server.repository;

import me.toy.server.dto.RecentDateCourseDto;
import me.toy.server.entity.DateCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DateCourseRepository extends JpaRepository <DateCourse,Long> {

    @Query("select d from DateCourse d left join fetch d.locations order by d.id")
    public List<RecentDateCourseDto> findRecentDatecourse();
}
