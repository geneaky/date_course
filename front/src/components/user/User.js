import React from "react";
import { useDispatch, useSelector } from "react-redux";
import styled from "styled-components";
import { toggleUserMenu } from "../../store/store";

const User = () => {
  const user = useSelector((store) => store.user);
  const dispatch = useDispatch();

  return (
    <StyledUserdiv
      onClick={() => {
        console.log("check");
        dispatch(toggleUserMenu());
      }}
    >
      <img src={user.profileImage} />
    </StyledUserdiv>
  );
};

const StyledUserdiv = styled.div`
  width: 2.7rem;
  height: 2.7rem;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`;

export default User;
