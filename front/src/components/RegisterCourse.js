import React, { useState } from "react";
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
  const course = useSelector((store) => store?.course);
  const [text, setText] = useState();
  const [tag, setTag] = useState();
  const [file, setFile] = useState([]);
  const onFile = (e) => {
    setFile([...file, e.target.files[0]]);
  };
  const onText = (e) => {
    setText(e.target.value);
  };
  const onTag = (e) => {
    setTag(e.target.value);
  };
  // const Posting = () => {
  //   const url = "/course";
  //   const formData = new FormData();
  //   formData.append("file", file);
  //   formData.append("text", text);
  //   formData.append("tag", tag);
  //   const config = {
  //     headers: { "content-type": "multipart/form-data" },
  //   };

  //   axios.post(url, formData, config);
  // };
  return (
    <div>
      <p>{course[0][0]}</p>
      <form action="/course" method="post" enctype="multipart/form-data">
        <input type="file" multiple onChange={onFile} />
        {/* 다중 이미지 미리보기 적용하고 사이트에서 광고창 넘기듯이 적용 */}
        <br />
        <textarea placeholder={"글 작성"} onChange={onText} />
        <br />
        <textarea placeholder={"태그 등록"} onChange={onTag} />
        <br />
        <span>
          {/* <button>이전코스</button> */}
          <button type="submit">등록</button>
          {/* <button>다음코스</button> */}
        </span>
      </form>
    </div>
  );
};

export default RegisterCourse;
