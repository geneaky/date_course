import React, { useState } from "react";
import styled from "styled-components";
import SearchCourse from "../components/SearchCourse";
import MakeCourse from "../components/MakeCourse";

const CourseMenu = () => {
  const [toggleButton, setToggleButton] = useState("코스 만들기");

  const presentButton = () => {
    if (toggleButton === "코스 만들기") {
      setToggleButton("코스 찾기");
    } else {
      setToggleButton("코스 만들기");
    }
  };
  return (
    <CourseMenuDiv>
      <CourseMenuToggleButton onClick={presentButton}>
        {toggleButton}
      </CourseMenuToggleButton>
      {toggleButton === "코스 만들기" ? <SearchCourse /> : <MakeCourse />}
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