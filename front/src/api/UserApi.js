import axios from "axios";
import {
  setMyCourseList,
  setSavedCourseList,
  setUserLikedCourse,
  setUserSavedCourse,
} from "../store/store";

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
  const response = await axios.get(url, config);
  dispatcher(setUserSavedCourse(response.data));
};

export const getMyCourseList = async (dispatcher) => {
  const token = localStorage.getItem("accessToken");
  const url = "/user/mycourse";
  const config = {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
  const response = await axios.get(url, config);
  dispatcher(setMyCourseList(response.data));
};

export const getSavedCourseList = async (dispatcher) => {
  const token = localStorage.getItem("accessToken");
  const url = "/user/savedcourse/list";
  const config = {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
  const response = await axios.get(url, config);
  dispatcher(setSavedCourseList(response.data));
};
