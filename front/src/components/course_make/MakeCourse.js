import React from "react";
import RegisterCourse from "./RegisterCourse";
import SearchBox from "./SearchBox";
import SearchResultBox from "./SearchResultBox";

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
