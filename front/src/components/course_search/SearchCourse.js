import React, { useEffect } from "react";
import { useSelector } from "react-redux";
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
    </div>
  );
};

export default SearchCourse;
