import React from "react";
import styled from "styled-components";

const UserMenuItem = ({ itemName }) => {
  return <StyledMenuItem>{itemName}</StyledMenuItem>;
};

export default UserMenuItem;

const StyledMenuItem = styled.div`
  height: 50px;
`;
