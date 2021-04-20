import React from "react";
import styled from "styled-components";
import { Link } from "react-router-dom";
import MenuIcon from "@material-ui/icons/Menu";

const Header = ({ sideMenu, setSideMenu }) => {
  const menuPop = () => {
    setSideMenu(!sideMenu);
  };

  return (
    <HeaderDiv>
      <MenuIcon onClick={menuPop} />
      <h3>open date course</h3>
      <StyledHeaderLink to="/login">로그인</StyledHeaderLink>
      <p>Guest</p>
    </HeaderDiv>
  );
};

const HeaderDiv = styled.div`
  display: flex;
  justify-content: space-between;
  height: 2.7rem;
  background-color: #ffa07a;
  * {
    margin: auto 20px;
  }
`;

const StyledHeaderLink = styled(Link)`
  text-decoration: none;
`;

export default Header;
