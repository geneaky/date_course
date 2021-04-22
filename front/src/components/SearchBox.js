/*global kakao*/
import React, { useEffect, useState } from "react";
import styled from "styled-components";
import SearchIcon from "@material-ui/icons/Search";
import { useDispatch, useSelector } from "react-redux";
import { clearMarker, setPlaces } from "../store/store";

const SearchBox = () => {
  const [searchResult, setSearchResult] = useState([]);
  const dispatcher = useDispatch();
  const store_place = useSelector((store) => store?.places);
  useEffect(() => {
    const places = new kakao.maps.services.Places();
    let callback = function (data, status) {
      if (status === kakao.maps.services.Status.OK) {
        let dataList = [];
        for (let i in data) {
          dataList.push([
            data[i].place_name,
            data[i].address_name,
            data[i].phone,
            data[i].x,
            data[i].y,
          ]);
        }
        dispatcher(setPlaces(dataList));
      }
    };
    places.keywordSearch(searchResult, callback);
  }, [searchResult]);

  const onKeyPress = (e) => {
    if (e.key === "Enter") {
      dispatcher(clearMarker());
      setSearchResult(e.target.value);
    }
  };

  return (
    <SearchBoxDiv>
      <SearchIcon />
      <input
        placeholder="장소 또는 지역 이름을 검색하세요"
        onKeyPress={onKeyPress}
      />
    </SearchBoxDiv>
  );
};

const SearchBoxDiv = styled.div`
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid lightgray;
  input {
    width: 100%;
    border: none;
    outline-style: none;
  }
`;

export default SearchBox;
