import React, { useState } from "react";
import styled from "styled-components";
import SearchCourse from "./course_search/SearchCourse";
import MakeCourse from "./course_make/MakeCourse";
import { useDispatch } from "react-redux";
import { resetCourse, resetPlaces } from "../store/store";

const CourseMenu = () => {
  const [toggleButton, setToggleButton] = useState("Make Course");
  const dispatcher = useDispatch();
  const presentButton = () => {
    if (toggleButton === "Make Course") {
      dispatcher(resetCourse());
      dispatcher(resetPlaces());
      setToggleButton("Search Course");
    } else {
      setToggleButton("Make Course");
    }
  };
  return (
    <CourseMenuDiv>
      <CourseMenuToggleButton onClick={presentButton}>
        {toggleButton}
      </CourseMenuToggleButton>
      {toggleButton === "Make Course" ? <SearchCourse /> : <MakeCourse />}
    </CourseMenuDiv>
  );
};

const CourseMenuDiv = styled.div`
  position: absolute;
  z-index: 999;
  background-color: white;
  border-radius: 3px;
  width: 29%;
  height: 90%;
`;

const CourseMenuToggleButton = styled.button`
  width: 100%;
  height: 7%;
  border: none;
  outline-style: none;
  cursor: pointer;
  background-color: #faf0e6;
`;

export default CourseMenu;
