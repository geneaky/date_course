import React, { useState } from "react";
import styled from "styled-components";
import axios from "axios";
import { useSelector } from "react-redux";

const RegisterForm = () => {
  const url = "/course";
  const formData = new FormData();
  const config = {
    headers: { "content-type": "multipart/form-data" },
  };

  axios.post(url, formData, config);
};

const RegisterCourse = () => {
  const course = useSelector((store) => store.course);

  return (
    <StyledRegisterCourse>
      <p>{course[0]?.placeName}</p>
      <StyledPhotoFeat>
        <label for="file">Upload Photo</label>
        <input type="file" id="file" multiple style={{ display: "none" }} />
        <button>Look Photo</button>
      </StyledPhotoFeat>
      {/* 다중 이미지 미리보기 적용하고 사이트에서 광고창 넘기듯이 적용 */}
      <textarea
        placeholder={"글 작성 및 해시태그 등록(해시태그는 정규표현식 사용)"}
      />
      <StyledChooseButton>
        <button>Back</button>
        <button>Decide</button>
        <button>Next</button>
      </StyledChooseButton>
      <button>Upload!</button>
    </StyledRegisterCourse>
  );
};

const StyledRegisterCourse = styled.div`
  * {
    display: block;
  }
  p {
    font-size: 5px;
    font-weight: 500;
    color: #000;
  }
  textarea {
    width: 91%;
    height: 200px;
    resize: none;
    border: 1px solid lightgray;
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
  margin: 0;
  padding: 0;
  label {
    width: 50%;
    vertical-align: middle;
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

export default RegisterCourse;
