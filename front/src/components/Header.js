import React from "react";
import styled from "styled-components";
import { Link } from "react-router-dom";
import MenuIcon from "@material-ui/icons/Menu";
import { useDispatch, useSelector } from "react-redux";
import { setSideMenu } from "../store/store";
import User from "./user/User";
import AccountCircleIcon from "@material-ui/icons/AccountCircle";

const Header = () => {
  const user = useSelector((store) => store.user);
  const dispatch = useDispatch();

  return (
    <HeaderDiv>
      <MenuIcon
        onClick={() => {
          dispatch(setSideMenu());
        }}
        style={menuStyle}
      />
      <h3>open date course</h3>
      {user === null ? (
        <>
          <StyledHeaderLink to="/login">로그인</StyledHeaderLink>
          <AccountCircleIcon />
        </>
      ) : (
        <User />
      )}
    </HeaderDiv>
  );
};

const HeaderDiv = styled.div`
  display: flex;
  justify-content: space-between;
  height: 3.2rem;
  background-color: #ffa07a;
  * {
    margin: auto 50px auto 0;
  }
`;

const StyledHeaderLink = styled(Link)`
  text-decoration: none;
`;

const menuStyle = {
  marginLeft: "50px",
};

export default Header;
