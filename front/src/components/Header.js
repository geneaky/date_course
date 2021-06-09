import React from "react";
import styled from "styled-components";
import { Link } from "react-router-dom";
import MenuIcon from "@material-ui/icons/Menu";
import { useDispatch, useSelector } from "react-redux";
import { setSideMenu } from "../store/store";
import User from "./user/User";
import AccountCircleIcon from "@material-ui/icons/AccountCircle";
import NotificationsIcon from "@material-ui/icons/Notifications";

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
      <h3>Open Date Course</h3>
      {user ? (
        <StlyedUserWithNotification>
          <NotificationsIcon />
          <User />
        </StlyedUserWithNotification>
      ) : (
        <StyledHeaderLink to="/login">
          <AccountCircleIcon />
          로그인
        </StyledHeaderLink>
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
    margin: auto 30px auto 0;
    cursor: pointer;
  }
`;

const StyledHeaderLink = styled(Link)`
  text-decoration: none;
`;

const StlyedUserWithNotification = styled.div`
  display: flex;
  justify-content: space-between;
`;

const menuStyle = {
  marginLeft: "50px",
};

export default Header;
