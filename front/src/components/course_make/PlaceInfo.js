/*global kakao*/
import React from "react";
import { useDispatch, useSelector } from "react-redux";
import styled from "styled-components";
import { setLocation, setMarker } from "../../store/store";

const PlaceInfo = ({ result }) => {
  const map = useSelector((store) => store.map);
  const selectedMarker = useSelector((store) => store.marker);
  const dispatcher = useDispatch();

  const onClick = () => {
    selectedMarker.forEach((marker) => {
      marker.setMap(null);
    });
    const marker = new kakao.maps.Marker({
      map: map,
      position: new kakao.maps.LatLng(result.posY, result.posX),
    });
    marker.setMap(map);
    dispatcher(setMarker(marker));
    map.setCenter(new kakao.maps.LatLng(result.posY, result.posX));
    dispatcher(
      setLocation({
        placeName: result.placeName,
        posX: result.posX,
        posY: result.posY,
      })
    );
  };

  return (
    <PlaceInfoDiv onClick={onClick}>
      <p>{result.placeName}</p>
      <p>{result.address}</p>
      <p>{result.phone}</p>
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
