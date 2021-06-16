import React, { useEffect } from "react";
import styled from "styled-components";
import { useDispatch, useSelector } from "react-redux";
import CourseInfo from "./CourseInfo";
import { searchRecentDateCourseList } from "../../api/DateCourseApi";
import { likedCourseList } from "../../api/UserApi";

const SearchResultBoxInSearchCourse = () => {
  const searchCourseList = useSelector((store) => store.searchCourseList);
  const dispatcher = useDispatch();

  useEffect(() => {
    searchRecentDateCourseList(dispatcher);
    likedCourseList(dispatcher);
  }, []);

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
