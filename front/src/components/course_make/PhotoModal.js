import React from "react";
import { useDispatch } from "react-redux";
import styled from "styled-components";
import { togglePhotoModal } from "../../store/store";
import PhotoSlider from "./PhotoSlider";

const PhotoModal = () => {
  const dispatch = useDispatch();
  return (
    <StyledPhotoModal
      onClick={() => {
        dispatch(togglePhotoModal());
      }}
    >
      <StyledModalContainer onClick={(e) => e.stopPropagation()}>
        <PhotoSlider />
      </StyledModalContainer>
    </StyledPhotoModal>
  );
};

const StyledPhotoModal = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: #00000080;
  z-index: 1000;
`;

const StyledModalContainer = styled.div`
  position: absolute;
  background: white;
  border: none;
  opacity: 70%;
  border-radius: 15px;
  width: 700px;
  height: 620px;
  left: 30%;
  top: 15%;
`;

export default PhotoModal;
