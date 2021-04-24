import styled from "styled-components";
import React, { createRef, useRef, useState } from "react";

const Login = () => {
  return (
    <div>
      <h3>페이지 접속을 위한 로그인</h3>
      <a href="http://localhost:8080/oauth2/authorization/google">
        {/* 나중에 https 주소 도메인 신청해서 바꾸자 */}
        구글 로그인
      </a>
    </div>
  );
};

export default Login;
