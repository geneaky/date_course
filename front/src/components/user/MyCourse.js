import React, { useEffect } from "react";
import { useDispatch } from "react-redux";
import styled from "styled-components";
import { getMyCourseList, likedCourseList } from "../../api/UserApi";
import MyCourseList from "./MyCourseList";
import Course from "../course_search/Course";

const MyCourse = () => {
  const dispatcher = useDispatch();
  useEffect(() => {
    getMyCourseList(dispatcher);
    likedCourseList(dispatcher);
  }, []);
  return (
    <StyledMyCourseDiv>
      <MyCourseList />
      <Course />
    </StyledMyCourseDiv>
  );
};

const StyledMyCourseDiv = styled.div`
  position: absolute;
  width: 29%;
  height: 78%;
  background-color: white;
  z-index: 999;
`;

export default MyCourse;
