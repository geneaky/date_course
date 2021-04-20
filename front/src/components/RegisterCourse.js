import React, { useContext, useState } from "react";
import { CourseContext } from "../store/course";
import axios from "axios";

const RegisterForm = () => {
  const url = "/course";
  const formData = new FormData();
  const config = {
    headers: { "content-type": "multipart/form-data" },
  };

  axios.post(url, formData, config);
};

const RegisterCourse = () => {
  const courseContext = useContext(CourseContext);
  const [form, setForm] = useState({ file: null, text: null, tag: null });
  const onFile = (e) => {
    setForm({ file: e.target.files[0], ...form });
  };
  const onText = (e) => {
    setForm({ ...form, text: e.target.value });
  };
  const onTag = (e) => {
    setForm({ ...form, tag: e.target.value });
  };
  const Posting = () => {
    const url = "/course";
    const formData = new FormData();
    console.log(form.file);
    formData.append("file", form.file);
    const config = {
      headers: { "content-type": "multipart/form-data" },
    };

    axios.post(url, formData, config);
  };
  return (
    <div>
      <p>{courseContext.state[0]}</p>
      <input type="file" multiple onChange={onFile} />
      {/* 다중 이미지 미리보기 적용하고 사이트에서 광고창 넘기듯이 적용 */}
      <br />
      <textarea value={"글 작성"} onChange={onText} />
      <br />
      <textarea value={"태그 등록"} onChange={onTag} />
      <br />
      <span>
        <button>이전코스</button>
        <button onClick={Posting}>등록</button>
        <button>다음코스</button>
      </span>
    </div>
  );
};

export default RegisterCourse;
