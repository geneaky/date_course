import axios from "axios";
import { setSearchCourseList } from "../store/store";

export const searchRecentDateCourseList = (dispatcher) => {
  const token = localStorage.getItem("accessToken");
  const url = "/datecourse/recent";
  const config = {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
  axios.get(url, config).then((response) => {
    dispatcher(setSearchCourseList(response.data));
  });
};
