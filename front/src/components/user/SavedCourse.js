import React, { useEffect } from "react";
import { useDispatch } from "react-redux";
import styled from "styled-components";
import {
  getSavedCourseList,
  likedCourseList,
  savedCourseList,
} from "../../api/UserApi";
import Course from "../course_search/Course";
import SavedCourseList from "./SavedCourseList";

const SavedCourse = () => {
  const dispatcher = useDispatch();
  useEffect(() => {
    getSavedCourseList(dispatcher);
    savedCourseList(dispatcher);
    likedCourseList(dispatcher);
  }, []);
  return (
    <StyledSavedCourseDiv>
      <SavedCourseList />
      <Course />
    </StyledSavedCourseDiv>
  );
};

const StyledSavedCourseDiv = styled.div`
  position: absolute;
  width: 29%;
  height: 78%;
  background-color: white;
  z-index: 999;
`;
export default SavedCourse;
