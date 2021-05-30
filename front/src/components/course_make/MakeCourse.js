import React from "react";
import RegisterCourse from "./RegisterCourse";
import SearchBoxInMakeCourse from "./SearchBoxInMakeCourse";
import SearchResultBoxInMakeCourse from "./SearchResultBoxInMakeCourse";

const MakeCourse = () => {
  return (
    <div>
      <SearchBoxInMakeCourse />
      <SearchResultBoxInMakeCourse />
      <RegisterCourse />
    </div>
  );
};

export default MakeCourse;
