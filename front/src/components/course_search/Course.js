import React from "react";
import { useSelector } from "react-redux";
import styled from "styled-components";

const s3Url = "/public/";
// s3 버킷 주소로 지금 바꾸자 걍 ;
const Course = () => {
  const selectedDatecourse = useSelector((store) => store.selectedDatecourse);
  console.log(selectedDatecourse);
  return (
    <div>
      {/* img는 s3 추가하고 반영*/}
      <p>{selectedDatecourse.thumUp}</p>
      {/* show tag */}
      {/* show text */}
      {/* comment 접기 or 스크롤  */}
    </div>
  );
};

export default Course;
