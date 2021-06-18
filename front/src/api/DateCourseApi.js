import axios from "axios";
import { setSearchCourseList } from "../store/store";

export const searchRecentDateCourseList = async (dispatcher) => {
  const token = localStorage.getItem("accessToken");
  const url = "/datecourse/recent";
  const config = {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
  const response = await axios.get(url, config);
  dispatcher(setSearchCourseList(response.data));
};

export const searchOptionDateCourseList = (dispatcher, option) => {
  const token = localStorage.getItem("accessToken");
  const url = `/datecourse/${option}`;
  const config = {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
  axios.get(url, config).then((response) => {
    dispatcher(setSearchCourseList(response.data));
  });
};
