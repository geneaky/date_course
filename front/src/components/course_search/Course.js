import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import CourseDetail from "./CourseDetail";

const Course = () => {
  const selectedDatecourse = useSelector((store) => store.selectedDatecourse);
  return (
    <div>
      {selectedDatecourse ? <CourseDetail course={selectedDatecourse} /> : null}
    </div>
  );
};

export default Course;
