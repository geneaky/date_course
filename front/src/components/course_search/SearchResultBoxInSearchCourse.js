import React, { useEffect } from "react";
import styled from "styled-components";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import { setSearchCourseList } from "../../store/store";
import CourseInfo from "./CourseInfo";

const SearchResultBoxInSearchCourse = () => {
  const searchCourseList = useSelector((store) => store.searchCourseList);
  const dispatcher = useDispatch();

  const searchRecentDateCourseList = () => {
    const token = localStorage.getItem("accessToken");
    const url = "/datecourse/recent";
    const config = {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
    axios.get(url, config).then((response) => {
      dispatcher(setSearchCourseList(response.data));
    });
  };
  useEffect(() => {
    searchRecentDateCourseList();
    console.log(searchCourseList);
  }, []);
  return (
    <SearchResultBoxDiv>
      {searchCourseList.map((course, index) => {
        <CourseInfo key={index} result={course} />;
      })}
    </SearchResultBoxDiv>
  );
};

const SearchResultBoxDiv = styled.div`
  height: 300px;
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
