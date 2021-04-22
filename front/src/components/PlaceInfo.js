/*global kakao*/
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import styled from "styled-components";
import { registerCourse, setMarker } from "../store/store";

const PlaceInfo = ({ result }) => {
  const map = useSelector((store) => store.map);
  const dispatcher = useDispatch();
  useEffect(() => {
    let marker = new kakao.maps.Marker({
      map: map,
      position: new kakao.maps.LatLng(result[4], result[3]),
    });
    marker.setMap(map);
    dispatcher(setMarker(marker));
  }, [result, map]);

  const onClick = () => {
    dispatcher(registerCourse(result));
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
