import React, { useState } from "react";
import styled from "styled-components";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import PhotoModal from "./PhotoModal";
import { registerCourse, togglePhotoModal } from "../../store/store";

const RegisterForm = (courses) => {
  const token = localStorage.getItem("accessToken");
  const url = "/datecourse";
  const config = {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
  courses.forEach(async (course) => {
    const formData = new FormData();
    Object.values(course.location.user.photos).forEach((photo) => {
      formData.append("files", photo);
    });
    formData.append(
      "course",
      new Blob(
        [
          JSON.stringify({
            placeName: "이수역",
            posX: "26",
            posY: "126",
            text: "hi",
          }),
        ],
        { type: "application/json" }
      )
    );
    await axios.post(url, formData, config);
  });
};

const RegisterCourse = () => {
  const [files, setFiles] = useState();
  const [text, setText] = useState();
  const courses = useSelector((store) => store.course);
  const location = useSelector((store) => store.location);
  const photoModal = useSelector((store) => store.photoModal);
  const dispatcher = useDispatch();

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
          RegisterForm(courses);
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
