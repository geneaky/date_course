import styled from "styled-components";
import React, { createRef, useRef, useState } from "react";

const Login = () => {
  return (
    <StyledLoginForm>
      <h2>Open DateCourse</h2>
      <p>간편하게 로그인하고</p>
      <h3>데이트 코스를 공유하세요</h3>
      {/* 나중에 https 주소 도메인 신청해서 바꾸자 */}
      <StyledLoginButton>
        <a href="http://3.34.83.151:3030/oauth2/authorize/google?redirect_uri=http://3.34.83.151:80/oauth2/redirect">
          <img
            alt="Google login"
            src="https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Google_%22G%22_Logo.svg/512px-Google_%22G%22_Logo.svg.png"
          />
          구글 로그인으로 시작
        </a>
      </StyledLoginButton>
    </StyledLoginForm>
  );
};

const StyledLoginForm = styled.div`
  border: 1px solid lightgray;
  border-radius: 3px;
  position: absolute;
  left: 35%;
  top: 20%;
  width: 25%;
  height: 500px;
`;

const StyledLoginButton = styled.div`
  border: 1px solid lightgray;
  border-radius: 3px;
  width: 60%;
  padding: 0;
  margin: 0 auto;
  img {
    width: 25px;
  }
  a {
    text-decoration: none;
    text-align: center;
    vertical-align: center;
  }
`;

export default Login;
