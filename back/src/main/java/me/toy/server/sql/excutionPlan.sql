select *
from course c
         left join location l on l.course_id = c.course_id
         left join user_course_like ucl on ucl.course_id = c.course_id;

select *
from course c
where c.course_id in
      (select c.course_id
       from course c
                join location l on l.course_id = c.course_id
                join location_tag lt on lt.location_id = l.location_id
                join tag t on t.tag_id = lt.tag_id
       where t.name = "ee");

select *
from user u
where u.user_id in (select f.followee_id from follow f where f.user_id = 3);

select *
from user u
where u.user_id in (select f.user_id from follow f where f.followee_id = 3);