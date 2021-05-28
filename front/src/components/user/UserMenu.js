import React from "react";
import styled from "styled-components";
import UserMenuItem from "./UserMenuItem";
import InfoIcon from "@material-ui/icons/Info";
import WcIcon from "@material-ui/icons/Wc";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";
import { useHistory } from "react-router-dom";

const UserMenu = () => {
  const history = useHistory();
  const logOut = () => {
    localStorage.removeItem("accessToken");
    history.go(0);
  };
  return (
    <StyledUserMenu>
      <div>
        <WcIcon />
        <UserMenuItem itemName={"내 경로"} />
      </div>
      <div>
        <InfoIcon />
        <UserMenuItem itemName={"내 정보"} />
      </div>
      <div>
        <ExitToAppIcon />
        <UserMenuItem itemName={"로그아웃"} onClick={logOut} />
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
