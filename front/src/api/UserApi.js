import axios from "axios";
import { setUserLikedCourse } from "../store/store";

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
