import React from "react";
import styled from "styled-components";
import UserMenuItem from "./UserMenuItem";
import GestureIcon from "@material-ui/icons/Gesture";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";
import BookmarksIcon from "@material-ui/icons/Bookmarks";
import { useHistory } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import {
  logoutUser,
  setSideMenu,
  toggleMyCourseMenu,
  toggleSavedCourseMenu,
} from "../../store/store";

const UserMenu = () => {
  const history = useHistory();
  const dispatcher = useDispatch();
  const myCourseMenu = useSelector((store) => store.myCourseMenu);
  const savedCourseMenu = useSelector((store) => store.savedCourseMenu);
  const logOut = () => {
    localStorage.removeItem("accessToken");
    dispatcher(logoutUser());
    history.push("/login");
  };

  const closeSideMenuAndOpenMyCourseMenu = () => {
    if (!myCourseMenu) {
      dispatcher(setSideMenu(false));
      dispatcher(toggleSavedCourseMenu(false));
      dispatcher(toggleMyCourseMenu(true));
    } else {
      dispatcher(toggleMyCourseMenu(false));
    }
  };

  const closeSideMenuAndOpenSavedCourseMenu = () => {
    if (!savedCourseMenu) {
      dispatcher(setSideMenu(false));
      dispatcher(toggleMyCourseMenu(false));
      dispatcher(toggleSavedCourseMenu(true));
    } else {
      dispatcher(toggleSavedCourseMenu(false));
    }
  };

  return (
    <StyledUserMenu>
      <div onClick={closeSideMenuAndOpenMyCourseMenu}>
        <GestureIcon />
        <UserMenuItem itemName={"My Course"} />
      </div>
      <div onClick={closeSideMenuAndOpenSavedCourseMenu}>
        <BookmarksIcon />
        <UserMenuItem itemName={"Saved Course"} />
      </div>
      <div>
        <ExitToAppIcon />
        <UserMenuItem itemName={"Logout"} props={logOut} />
      </div>
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

export default UserMenu;
