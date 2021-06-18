import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { searchOptionDateCourseList } from "../../api/DateCourseApi";
import { likedCourseList, savedCourseList } from "../../api/UserApi";
import Course from "./Course";
import SearchBoxInSearchCourse from "./SearchBoxInSearchCourse";
import SearchResultBoxInSearchCourse from "./SearchResultBoxInSearchCourse";

const SearchCourse = () => {
  const selectedMarker = useSelector((store) => store.marker);
  const searchCourseList = useSelector((store) => store.searchCourseList);
  const searchOption = useSelector((store) => store.searchOption);
  const dispatcher = useDispatch();

  useEffect(() => {
    selectedMarker.forEach((marker) => {
      marker.setMap(null);
    });
    searchOptionDateCourseList(dispatcher, searchOption);
    savedCourseList(dispatcher);
    likedCourseList(dispatcher);
  }, []);

  return (
    <div>
      <SearchBoxInSearchCourse />
      <SearchResultBoxInSearchCourse searchCourseList={searchCourseList} />
      <Course />
    </div>
  );
};

export default SearchCourse;
