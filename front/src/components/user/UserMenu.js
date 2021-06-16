import React from "react";
import styled from "styled-components";
import UserMenuItem from "./UserMenuItem";
import { Link } from "react-router-dom";
import GestureIcon from "@material-ui/icons/Gesture";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";
import BookmarksIcon from "@material-ui/icons/Bookmarks";
import { useHistory } from "react-router-dom";
import { useDispatch } from "react-redux";
import { logoutUser } from "../../store/store";

const UserMenu = () => {
  const history = useHistory();
  const dispatcher = useDispatch();
  const logOut = () => {
    localStorage.removeItem("accessToken");
    dispatcher(logoutUser());
    history.push("/login");
  };
  return (
    <StyledUserMenu>
      <StyledUserMenuLink to="/myCourse">
        <GestureIcon />
        <UserMenuItem itemName={"My Course"} />
      </StyledUserMenuLink>
      <StyledUserMenuLink to="/savedCourse">
        <BookmarksIcon />
        <UserMenuItem itemName={"Saved Course"} />
      </StyledUserMenuLink>
      <StyledUserMenuLink>
        <ExitToAppIcon />
        <UserMenuItem itemName={"Logout"} props={logOut} />
      </StyledUserMenuLink>
    </StyledUserMenu>
  );
};

const StyledUserMenu = styled.div`
  position: absolute;
  right: 30px;
  top: 57px;
  background-color: #faf0e6;
  border-radius: 3px;
  width: 10%;
  height: 150px;
  z-index: 2;

  * {
    display: flex;
    margin: 0 auto;
    cursor: pointer;
    :hover {
      background-color: white;
    }
    align-items: center;
  }
`;

const StyledUserMenuLink = styled(Link)`
  text-decoration: none;
  color: black;
`;

export default UserMenu;
