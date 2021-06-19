import React from "react";
import { useSelector } from "react-redux";
import styled from "styled-components";
import CourseInfo from "../course_search/CourseInfo";

const SavedCourseList = () => {
  const savedCourseList = useSelector((store) => store.savedCourseList);
  console.log(savedCourseList);
  return (
    <StyledMyCourseListDiv>
      {savedCourseList?.map((course, index) => (
        <CourseInfo key={index} result={course} />
      ))}
    </StyledMyCourseListDiv>
  );
};

const StyledMyCourseListDiv = styled.div`
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

export default SavedCourseList;
