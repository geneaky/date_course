import React from "react";
import { useSelector } from "react-redux";
import CourseDetail from "./CourseDetail";

const Course = () => {
  const selectedCourse = useSelector((store) => store.selectedDatecourse);
  return (
    <div>
      {selectedCourse ? <CourseDetail course={selectedCourse} /> : null}
    </div>
  );
};

export default Course;
