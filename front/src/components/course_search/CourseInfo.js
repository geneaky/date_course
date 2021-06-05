/*global kakao*/
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import styled from "styled-components";
import { setMarker, setSelectedDatecourse } from "../../store/store";

const CourseInfo = ({ result }) => {
  const map = useSelector((store) => store.map);
  const selectedMarker = useSelector((store) => store.marker);
  const dispatcher = useDispatch();

  useEffect(() => {
    selectedMarker.forEach((marker) => {
      marker.setMap(null);
    });
  }, []);

  const initiateDatecourse = () => {
    selectedMarker.forEach((marker) => {
      marker.setMap(null);
    });
    result.locations.forEach((location) => {
      let marker = new kakao.maps.Marker({
        map: map,
        position: new kakao.maps.LatLng(location.posy, location.posx),
      });
      marker.setMap(map);
      dispatcher(setMarker(marker));
    });
    map.setCenter(
      new kakao.maps.LatLng(result.locations[0].posy, result.locations[0].posx)
    );
    dispatcher(setSelectedDatecourse(result));
  };
  return (
    <StyledCourseInfoDiv onClick={initiateDatecourse}>
      <p>{result.dateCourseTitle}</p>
      <p>{result.thumbUp}</p>
      {/* 댓글 카운트 */}
      {/* 작성자 닉네임 표시 */}
    </StyledCourseInfoDiv>
  );
};

const StyledCourseInfoDiv = styled.div`
  margin: 10px auto;
  cursor: pointer;
  border: 1px solid lightgray;
  border-radius: 3px;
  background-color: white;
  * {
    margin: 0 auto;
    padding: 0;
    text-align: center;
    font-size: small;
    font-weight: 400;
  }
  &:hover {
    border: 2px solid black;
  }
`;

export default CourseInfo;
