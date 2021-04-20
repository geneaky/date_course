/*global kakao*/
import React, { useEffect, useState, useContext } from "react";
import styled from "styled-components";
import SearchIcon from "@material-ui/icons/Search";
import { SearchContext } from "../store/search";
import { MarkerContext } from "../store/marker";

const SearchBox = () => {
  const [searchResult, setSearchResult] = useState([]);
  const context = useContext(SearchContext);
  const marker = useContext(MarkerContext);

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
        context.dispatch({ type: "SET_PLACES", places: dataList });
      }
    };
    places.keywordSearch(searchResult, callback);
  }, [searchResult]);

  const onKeyPress = (e) => {
    if (e.key === "Enter") {
      marker.dispatch({ type: "CLEAR_MARKERS" });
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
