import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import CourseDetail from "./CourseDetail";

const Course = () => {
  const selectedDatecourse = useSelector((store) => store.selectedDatecourse);
  // 선택시에 넣어둔 데이트 코스 즉 수정 후 다시 클릭하지 않으면 이전의 값이 유지되어있음
  // 클릭이 아니라 다른 방식으로 값을 넣어주고 리렌더링 시켜야함 어떻게..? 데이터가 변했다는걸 어떻게 알려주지?
  return (
    <div>
      {selectedDatecourse ? <CourseDetail course={selectedDatecourse} /> : null}
    </div>
  );
};

export default Course;
