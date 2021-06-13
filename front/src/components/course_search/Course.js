import React from "react";
import { useSelector } from "react-redux";
import CourseDetail from "./CourseDetail";

const Course = () => {
  const selectedDatecourse = useSelector((store) => store.selectedDatecourse);
  console.log(selectedDatecourse);
  return (
    <div>
      {selectedDatecourse ? <CourseDetail course={selectedDatecourse} /> : null}
    </div>
  );
};

export default Course;
