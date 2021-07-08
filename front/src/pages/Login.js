import styled from "styled-components";
import React from "react";

const Login = () => {
  return (
    <StyledLoginForm>
      <h2>Open DateCourse</h2>
      <p>간편하게 로그인하고</p>
      <h3>데이트 코스를 공유하세요</h3>
      <StyledLoginButton>
        <a href="http://opendatecourse.duckdns.org:3030/oauth2/authorize/google?redirect_uri=http://opendatecourse.duckdns.org/oauth2/redirect">
          {/* <a href="http://localhost:8080/oauth2/authorize/google?redirect_uri=http://localhost:3000/oauth2/redirect"> */}
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
