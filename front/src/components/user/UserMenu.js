import React from "react";
import styled from "styled-components";

const UserMenu = () => {
  return (
    <StyledUserMenu>
      <p>내 경로</p>
      <p>내 정보</p>
      <p>알림</p>
      <p>내 계정</p>
      <p>로그아웃</p>
    </StyledUserMenu>
  );
};

const StyledUserMenu = styled.div`
  position: absolute;
  right: 30px;
  top: 57px;
  background-color: gray;
  width: 20%;
  height: 500px;
  z-index: 2;
`;

export default UserMenu;
