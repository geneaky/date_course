/*global kakao*/
import React, { useEffect, useState } from "react";
import styled from "styled-components";
import SearchIcon from "@material-ui/icons/Search";
import { useDispatch, useSelector } from "react-redux";
import { setPlaces } from "../../store/store";

const SearchBox = () => {
  const [searchResult, setSearchResult] = useState([]);
  const markers = useSelector((store) => store.marker);
  const dispatcher = useDispatch();
  useEffect(() => {
    const places = new kakao.maps.services.Places();
    let callback = function (data, status) {
      if (status === kakao.maps.services.Status.OK) {
        let dataList = [];
        for (let i in data) {
          dataList.push({
            placeName: data[i].place_name,
            address: data[i].address_name,
            phone: data[i].phone,
            posX: data[i].x,
            posY: data[i].y,
          });
        }
        dispatcher(setPlaces(dataList));
      }
    };
    places.keywordSearch(searchResult, callback);
  }, [searchResult]);

  const onKeyPress = (e) => {
    if (e.key === "Enter") {
      markers.forEach((marker) => {
        marker.setMap(null);
      });
      setSearchResult(e.target.value);
    }
  };

  return (
    <SearchBoxDiv>
      <SearchIcon />
      <SearchInput
        placeholder="장소 또는 지역 이름을 검색하세요"
        onKeyPress={onKeyPress}
      />
    </SearchBoxDiv>
  );
};

const SearchBoxDiv = styled.div`
  margin: 0;
  padding: 10px;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid lightgray;
`;

const SearchInput = styled.input`
  width: 100%;
  border: none;
  outline-style: none;
  font-size: 12pt;
`;

export default SearchBox;
