import axios from "axios";
import { setSearchCourseList } from "../store/store";

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
