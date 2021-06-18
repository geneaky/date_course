import React from "react";
import styled from "styled-components";
import CourseInfo from "./CourseInfo";

const SearchResultBoxInSearchCourse = ({ searchCourseList }) => {
  return (
    <SearchResultBoxDiv>
      {searchCourseList.map((course, index) => (
        <CourseInfo key={index} result={course} />
      ))}
    </SearchResultBoxDiv>
  );
};

const SearchResultBoxDiv = styled.div`
  height: 170px;
  overflow-y: scroll;
  border-bottom: 1px solid lightgray;
  background-color: #faf0e6;
  ::-webkit-scrollbar {
    width: 7px;
  }
  ::-webkit-scrollbar-track {
    background-color: none;
  }
  ::-webkit-scrollbar-thumb {
    background-color: #ffdab9;
    border-radius: 5px;
  }
`;

export default SearchResultBoxInSearchCourse;
