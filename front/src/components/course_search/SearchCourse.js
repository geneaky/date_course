import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import Course from "./Course";
import SearchBoxInSearchCourse from "./SearchBoxInSearchCourse";
import SearchResultBoxInSearchCourse from "./SearchResultBoxInSearchCourse";

const SearchCourse = () => {
  const selectedMarker = useSelector((store) => store.marker);

  useEffect(() => {
    selectedMarker.forEach((marker) => {
      marker.setMap(null);
    });
  }, []);
  return (
    <div>
      <SearchBoxInSearchCourse />
      <SearchResultBoxInSearchCourse />
      <Course />
    </div>
  );
};

export default SearchCourse;
