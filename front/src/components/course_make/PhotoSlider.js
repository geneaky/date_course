import React from "react";
import Slider from "react-slick";
import styled from "styled-components";

const slid = [1, 2, 3, 4];

const PhotoSlider = () => {
  return (
    <StyledPhotoSlider {...settings}>
      {slid.map((one) => (
        <div>
          <h1>{one}</h1>
        </div>
      ))}
    </StyledPhotoSlider>
  );
};

const settings = {
  dots: false,
  fade: true,
  infinite: true,
  arrows: false,
  speed: 500,
  slidesToShow: 1,
  slidesToScroll: 1,
};

const StyledPhotoSlider = styled(Slider)`
  * {
    position: absolute;
    left: 15%;
    top: 5%;
    h1 {
      width: 500px;
      height: 500px;
    }
  }
`;

export default PhotoSlider;
