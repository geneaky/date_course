import styled from "styled-components";
import React, { createRef, useEffect, useMemo, useRef, useState } from "react";

const Title = styled.h1`
  font-size: 1.5rem;
  text-align: center;
  color: palevioletred;
`;

const Login = () => {
  const myRef = useRef(null);
  const [list, setList] = useState([
    { id: 1, name: "길동" },
    { id: 2, name: "꺽정" },
  ]);

  const myRefs = Array.from({ length: list.length }).map(() => createRef());
  return (
    <div className="login">
      <h3>페이지 접속을 위한 로그인</h3>
      <a href="http://localhost:8080/oauth2/authorization/google">
        {/* 나중에 https 주소 도메인 신청해서 바꾸자 */}
        구글 로그인
      </a>
      <div ref={myRef}>박스</div>
      <button
        onClick={() => {
          myRef.current.style.backgroundColor = "red";
          myRefs[0].current.style.backgroundColor = "red";
          myRefs[1].current.style.backgroundColor = "blue";
        }}
      >
        색 변경
      </button>
      {list.map((user, index) => (
        <h1 ref={myRefs[index]}>{user.name}</h1>
      ))}
      <Title>hello</Title>
    </div>
  );
};

export default Login;
