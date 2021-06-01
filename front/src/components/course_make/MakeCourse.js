import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import RegisterCourse from "./RegisterCourse";
import SearchBoxInMakeCourse from "./SearchBoxInMakeCourse";
import SearchResultBoxInMakeCourse from "./SearchResultBoxInMakeCourse";

const MakeCourse = () => {
  const selectedMarker = useSelector((store) => store.marker);

  useEffect(() => {
    selectedMarker.forEach((marker) => {
      marker.setMap(null);
    });
  }, []);
  return (
    <div>
      <SearchBoxInMakeCourse />
      <SearchResultBoxInMakeCourse />
      <RegisterCourse />
    </div>
  );
};

export default MakeCourse;
