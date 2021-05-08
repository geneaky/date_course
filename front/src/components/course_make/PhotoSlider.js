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
  arrows: true,
  speed: 500,
  slidesToShow: 1,
  slidesToScroll: 1,
};

const StyledPhotoSlider = styled(Slider)`
  * {
    position: absolute;
    left: 50%;
  }
`;

export default PhotoSlider;
