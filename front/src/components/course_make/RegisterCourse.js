import React, { useState } from "react";
import styled from "styled-components";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import PhotoModal from "./PhotoModal";
import { registerCourse, togglePhotoModal } from "../../store/store";

const RegisterForm = async (course) => {
  const token = localStorage.getItem("accessToken");
  const url = "/datecourse";
  const config = {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };

  await axios.post(url, course, config);
};

const RegisterCourse = () => {
  const [files, setFiles] = useState();
  const [text, setText] = useState();
  const course = useSelector((store) => store.course);
  const location = useSelector((store) => store.location);
  const photoModal = useSelector((store) => store.photoModal);
  const dispatcher = useDispatch();
  /*
  flow 
  파일 여러개 선택 -> 텍스트 입력 -> 태그 입력 -> look photo클릭 후 사진 점검 -> decide 누르면 course배열에 location 하나를 추가
  location store를 하나 생성해서 로케이션이 여러개 모여 하나의 코스를 구성하는 방식으로 변경 
*/

  const decideLocation = () => {
    dispatcher(
      registerCourse({
        location: {
          place: location.place,
          user: { photos: files, text: text },
        },
      })
    );
    setFiles(null);
    setText("");
  };

  return (
    <StyledRegisterCourse>
      {photoModal ? <PhotoModal /> : null}
      <StyledPhotoFeat>
        <label htmlFor="file">Upload Photo</label>
        <input
          type="file"
          id="file"
          multiple
          style={{ display: "none" }}
          onChange={(e) => {
            setFiles(e.target.files);
          }}
        />
        <button onClick={() => dispatcher(togglePhotoModal())}>
          Look Photo
        </button>
      </StyledPhotoFeat>
      <textarea value={text} onChange={(e) => setText(e.target.value)} />
      <StyledChooseButton>
        <button>Back</button>
        <button onClick={decideLocation}>Next</button>
      </StyledChooseButton>
      <button
        onClick={() => {
          RegisterForm(course);
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
    margin: 0 1px;

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
    margin: 0 2px;

    :hover {
      background-color: #ffa07a;
      box-shadow: 0px 15px 20px rgba(223, 101, 45, 0.835);
      color: #fff;
      transform: translateY(-7px);
    }
  }
`;

export default RegisterCourse;
