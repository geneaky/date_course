import React, { useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import styled from "styled-components";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import PhotoModal from "./PhotoModal";
import {
  registerCourse,
  resetCourse,
  resetPlaces,
  togglePhotoModal,
} from "../../store/store";

const RegisterCourse = () => {
  const [file, setFile] = useState();
  const [text, setText] = useState();
  const [hashTag, setHashTag] = useState([]);
  const [title, setTitle] = useState();
  const courses = useSelector((store) => store.course);
  const location = useSelector((store) => store.location);
  const place = useSelector((store) => store.place);
  const photoModal = useSelector((store) => store.photoModal);
  const dispatcher = useDispatch();
  const history = useHistory();

  // useEffect(() => {}, [courses, location, place]); // refresh courses,location

  const RegisterForm = async (courses) => {
    const token = localStorage.getItem("accessToken");
    const url = "/datecourse";
    const config = {
      headers: {
        Authorization: `Bearer ${token}`,
        contentType: "multipart/form-data",
      },
    };
    const formData = new FormData();
    courses.forEach((course, index) => {
      if (course.location.user.photos) {
        formData.append(
          `locationList[${index}].file`,
          course.location.user.photos[0]
        );
      }
      formData.append(
        `locationList[${index}].placeName`,
        course.location.place.placeName
      );
      formData.append(
        `locationList[${index}].posX`,
        course.location.place.posX
      );
      formData.append(
        `locationList[${index}].posY`,
        course.location.place.posY
      );
      formData.append(`locationList[${index}].text`, course.location.user.text);
      formData.append(`locationList[${index}].hashTag`, hashTag[index]);
    });
    formData.append("courseTitle", title);
    await axios.post(url, formData, config);
  };

  const decideLocation = () => {
    if (location.place.placeName) {
      const hash = Array.from(new Set(text?.match(/(#[^\s#]+)/g)));
      const mainText = text?.replace(/#[^\s#]+/g, "").trim();
      console.log(mainText);
      console.log(hash);
      setHashTag([...hashTag, hash]);
      dispatcher(
        registerCourse({
          location: {
            place: location.place,
            user: { photos: file, text: mainText },
          },
        })
      );
      dispatcher(resetPlaces());
      setFile(null);
      setText("");
    } else {
      alert("데이트 장소를 골라주세요");
    }
  };

  return (
    <StyledRegisterCourse>
      {photoModal ? <PhotoModal /> : null}
      <StyledPhotoFeat>
        <label htmlFor="file">Upload Photo</label>
        <input
          type="file"
          id="file"
          style={{ display: "none" }}
          onChange={(e) => {
            setFile(e.target.files);
          }}
        />
        <button onClick={() => dispatcher(togglePhotoModal())}>
          Look Photo
        </button>
      </StyledPhotoFeat>
      <input
        placeholder="제목"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
      />
      <textarea value={text} onChange={(e) => setText(e.target.value)} />
      <StyledChooseButton>
        <button
          onClick={() => {
            history.go(0);
          }}
        >
          RESET
        </button>
        <button onClick={decideLocation}>Next LOCATION</button>
      </StyledChooseButton>
      <button
        onClick={() => {
          if (courses.length !== 0) {
            if (title != null) {
              RegisterForm(courses);
              dispatcher(resetCourse());
              history.go(0);
            } else {
              alert("제목을 등록해주세요");
            }
          } else {
            alert("코스를 등록해주세요");
          }
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
  input {
    width: 91%;
    border: none;
    outline: none;
    font-size: 16px;
    padding: 10px;
    margin: auto 12px;
  }
  textarea {
    width: 91%;
    height: 181px;
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
