/*global kakao*/
import React, { useEffect, useContext } from "react";
import styled from "styled-components";
import { MapContext } from "../store/map";
import { MarkerContext } from "../store/marker";
import { CourseContext } from "../store/course";

const PlaceInfo = ({ result }) => {
  const map = useContext(MapContext);
  const markerContext = useContext(MarkerContext);
  const courseContext = useContext(CourseContext);
  useEffect(() => {
    let marker = new kakao.maps.Marker({
      map: map.state,
      position: new kakao.maps.LatLng(result[4], result[3]),
    });
    marker.setMap(map.state);
    markerContext.dispatch({ type: "SET_MARKER", marker: marker });
  }, [result, map.state]);

  const onClick = () => {
    courseContext.dispatch({ type: "REGISTER_COURSE", course: result });
  };

  return (
    <PlaceInfoDiv onClick={onClick}>
      <p>{result[0]}</p>
      <p>{result[1]}</p>
      <p>{result[2]}</p>
    </PlaceInfoDiv>
  );
};

const PlaceInfoDiv = styled.div`
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

export default PlaceInfo;
