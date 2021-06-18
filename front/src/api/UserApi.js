import axios from "axios";
import { setUserLikedCourse, setUserSavedCourse } from "../store/store";

export const likedCourseList = async (dispatcher) => {
  const token = localStorage.getItem("accessToken");
  const url = "/user/likecourse";
  const config = {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
  const response = await axios.get(url, config);
  dispatcher(setUserLikedCourse(response.data));
};

export const savedCourseList = async (dispatcher) => {
  const token = localStorage.getItem("accessToken");
  const url = "/user/savedcourse";
  const config = {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
  const response = axios.get(url, config);
  dispatcher(setUserSavedCourse(response.data));
};
