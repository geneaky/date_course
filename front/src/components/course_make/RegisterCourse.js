import React, { useState } from "react";
import styled from "styled-components";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import PhotoModal from "./PhotoModal";
import {
  clearLocation,
  registerCourse,
  setLocationInfo,
  setLocation,
  togglePhotoModal,
} from "../../store/store";

const RegisterForm = () => {
  const url = "/course";
  const formData = new FormData();
  const config = {
    headers: { "content-type": "multipart/form-data" },
  };

  axios.post(url, formData, config);
};
// 코스안에는 여러개의 장소 여러개의 사진 여러개의 태그 여러개의 문장
/*
  const course = [
    {
      placeName: 1개 ,
      tag: n개,
      photo: n개,
      text: ,
    },
    {
      placeName: 1개,
      tag: n개,
      photo: n개,
      text: ,
    }
  ]

*/

const RegisterCourse = () => {
  const [files, setFiles] = useState();
  const [text, setText] = useState();
  const course = useSelector((store) => store.course);
  const location = useSelector((store) => store.location);
  const photoModal = useSelector((store) => store.photoModal);
  const dispatch = useDispatch();
  /*
  flow 
  파일 여러개 선택 -> 텍스트 입력 -> 태그 입력 -> look photo클릭 후 사진 점검 -> decide 누르면 course배열에 location 하나를 추가
  location store를 하나 생성해서 로케이션이 여러개 모여 하나의 코스를 구성하는 방식으로 변경 
*/
  const decideLocation = () => {
    dispatch(setLocationInfo({ photos: files, text: text }));
    dispatch(setLocation({ test: "test" }));
    console.log(location);
    dispatch(registerCourse(location));
    dispatch(clearLocation());
    setFiles(null);
    setText(null);
  };
  return (
    <StyledRegisterCourse>
      {photoModal ? <PhotoModal /> : null}
      <StyledPhotoFeat>
        <label for="file">Upload Photo</label>
        <input
          type="file"
          id="file"
          multiple
          style={{ display: "none" }}
          onChange={(e) => {
            setFiles(e.target.files);
          }}
        />
        <button onClick={() => dispatch(togglePhotoModal())}>Look Photo</button>
      </StyledPhotoFeat>
      <textarea
        placeholder={"글 작성 및 해시태그 등록(해시태그는 정규표현식 사용)"}
        onChange={(e) => setText(e.target.value)}
      />
      <StyledChooseButton>
        <button>Back</button>
        <button onClick={decideLocation}>Decide</button>
        <button>Next</button>
      </StyledChooseButton>
      <button
        onClick={() => {
          console.log(files);
          console.log(text);
        }}
      >
        Upload!
      </button>
    </StyledRegisterCourse>
  );
};

const StyledRegisterCourse = styled.div`
  * {
    display: block;
  }
  textarea {
    width: 91%;
    height: 222px;
    resize: none;
    border: 2px solid lightcoral;
    outline: none;
    border-radius: 5px;
    font-size: 16px;
    padding: 10px;
    margin: auto 12px;
  }
  button {
    width: 100%;
    height: 40px;
    font-family: "Roboto", sans-serif;
    font-size: 11px;
    text-transform: uppercase;
    letter-spacing: 2.5px;
    font-weight: 500;
    color: #000;
    background-color: #ffdab9;
    border: none;
    box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease 0s;
    cursor: pointer;
    outline: none;

    :hover {
      background-color: #ffa07a;
      box-shadow: 0px 15px 20px rgba(223, 101, 45, 0.835);
      color: #fff;
      transform: translateY(-7px);
    }
  }
`;

const StyledChooseButton = styled.div`
  display: flex;
  justify-content: center;
  button {
    width: 31%;
    height: 40px;
    font-family: "Roboto", sans-serif;
    font-size: 11px;
    text-transform: uppercase;
    letter-spacing: 2.5px;
    font-weight: 500;
    color: #000;
    background-color: #ffdab9;
    border: none;
    box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease 0s;
    cursor: pointer;
    outline: none;
    margin: 0 3px;

    :hover {
      background-color: #ffa07a;
      box-shadow: 0px 15px 20px rgba(223, 101, 45, 0.835);
      color: #fff;
      transform: translateY(-7px);
    }
  }
`;

const StyledPhotoFeat = styled.div`
  display: flex;
  label {
    width: 50%;
    height: 40px;
    line-height: 40px;
    font-family: "Roboto", sans-serif;
    font-size: 11px;
    text-transform: uppercase;
    letter-spacing: 2.5px;
    font-weight: 500;
    color: #000;
    background-color: #ffdab9;
    border: none;
    box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease 0s;
    cursor: pointer;
    outline: none;

    :hover {
      background-color: #ffa07a;
      box-shadow: 0px 15px 20px rgba(223, 101, 45, 0.835);
      color: #fff;
      transform: translateY(-7px);
    }
  }
  button {
    width: 50%;
    height: 40px;
    font-family: "Roboto", sans-serif;
    font-size: 11px;
    text-transform: uppercase;
    letter-spacing: 2.5px;
    font-weight: 500;
    color: #000;
    background-color: #ffdab9;
    border: none;
    box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease 0s;
    cursor: pointer;
    outline: none;
    margin: 0 3px;

    :hover {
      background-color: #ffa07a;
      box-shadow: 0px 15px 20px rgba(223, 101, 45, 0.835);
      color: #fff;
      transform: translateY(-7px);
    }
  }
`;

export default RegisterCourse;
