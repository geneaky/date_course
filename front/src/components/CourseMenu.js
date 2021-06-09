import React, { useState } from "react";
import styled from "styled-components";
import SearchCourse from "./course_search/SearchCourse";
import MakeCourse from "./course_make/MakeCourse";
import { useDispatch, useSelector } from "react-redux";
import { resetCourse, resetPlaces } from "../store/store";

const CourseMenu = () => {
  const [toggleButton, setToggleButton] = useState("코스 만들기");
  const user = useSelector((store) => store.user);
  const dispatcher = useDispatch();
  const presentButton = () => {
    //user 있는 상태에서 코스 만들기 ,코스 찾기 전환은 자유
    //user 없는 상태에서 코스 전환은 불가능
    console.log(user);
    if (user) {
      if (toggleButton === "코스 만들기") {
        dispatcher(resetCourse());
        dispatcher(resetPlaces());
        setToggleButton("코스 찾기");
      } else {
        setToggleButton("코스 만들기");
      }
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
