import React, { useContext } from "react";
import styled from "styled-components";
import PlaceInfo from "./PlaceInfo";
import { SearchContext } from "../store/search";

const SearchResultBox = () => {
  const context = useContext(SearchContext);

  return (
    <SearchResultBoxDiv>
      {context.state.map((place, index) => (
        <PlaceInfo key={index} result={place} />
      ))}
    </SearchResultBoxDiv>
  );
};

const SearchResultBoxDiv = styled.div`
  height: 300px;
  overflow-y: scroll;
  border-bottom: 1px solid lightgray;
  background-color: #faf0e6;
  ::-webkit-scrollbar {
    width: 7px;
  }
  ::-webkit-scrollbar-track {
    background-color: none;
  }
  ::-webkit-scrollbar-thumb {
    background-color: #ffdab9;
    border-radius: 5px;
  }
`;

export default SearchResultBox;
