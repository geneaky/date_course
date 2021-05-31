import React, { useEffect } from "react";
import styled from "styled-components";

const CourseInfo = ({ result }) => {
  console.log(result.dateCourseTitle);
  return (
    <StyledCourseInfoDiv>
      <p>{result.dateCourseTitle}</p>
    </StyledCourseInfoDiv>
  );
};

const StyledCourseInfoDiv = styled.div`
  margin: 10px auto;
  cursor: pointer;
  border: 1px solid lightgray;
  border-radius: 3px;
  background-color: white;
  * {
    margin: 0 auto;
    padding: 0;
    text-align: center;
    font-size: small;
    font-weight: 400;
  }
  &:hover {
    border: 2px solid black;
  }
`;

export default CourseInfo;
