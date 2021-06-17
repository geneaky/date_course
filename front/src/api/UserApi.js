import axios from "axios";
import { setUserLikedCourse, setUserSavedCourse } from "../store/store";

export const likedCourseList = (dispatcher) => {
  const token = localStorage.getItem("accessToken");
  const url = "/user/likecourse";
  const config = {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
  axios.get(url, config).then((response) => {
    dispatcher(setUserLikedCourse(response.data));
  });
};

export const savedCourseList = (dispatcher) => {
  const token = localStorage.getItem("accessToken");
  const url = "/user/savedcourse";
  const config = {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
  axios.get(url, config).then((response) => {
    dispatcher(setUserSavedCourse(response.data));
  });
};
