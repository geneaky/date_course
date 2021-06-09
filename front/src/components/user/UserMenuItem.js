import React from "react";
import styled from "styled-components";

const UserMenuItem = ({ itemName, props }) => {
  return <StyledMenuItem onClick={props}>{itemName}</StyledMenuItem>;
};

export default UserMenuItem;

const StyledMenuItem = styled.div`
  height: 50px;
`;
