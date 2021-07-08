import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import Slider from "react-slick";
import styled from "styled-components";

const PhotoSlider = () => {
  const [previewImg, setPreviewImg] = useState([]);
  const previewPhotos = useSelector((store) => store.previewPhotos);

  useEffect(() => {
    let newPhotos = [];
    previewPhotos.map((photo) => {
      newPhotos = [...newPhotos, URL.createObjectURL(photo[0])];
    });
    setPreviewImg(newPhotos);
  }, [previewPhotos]);

  return (
    <StyledPhotoSlider {...settings}>
      {previewImg.map((img) => (
        <div>
          <img src={img} />
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
    top: 10%;
    img {
      width: 500px;
      height: 500px;
    }
  }
`;

export default PhotoSlider;
