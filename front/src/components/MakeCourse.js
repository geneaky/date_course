import React from "react";
import SearchBox from "./SearchBox";
import SearchResultBox from "./SearchResultBox";
import RegisterCourse from "./RegisterCourse";

const MakeCourse = () => {
  return (
    <div>
      <SearchBox />
      <SearchResultBox />
      <RegisterCourse />
    </div>
  );
};

export default MakeCourse;
