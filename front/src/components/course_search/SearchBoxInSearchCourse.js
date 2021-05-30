import React from "react";
import styled from "styled-components";
import SearchIcon from "@material-ui/icons/Search";

const SearchBoxInSearchCourse = () => {
  return (
    <StyledSearchBoxDiv>
      <SearchIcon />
      <StyledSelectOption>
        <option>최신순</option>
        <option>인기순</option>
        <option>거리순</option>
      </StyledSelectOption>
      <StyledSearchInput placeholder="#검색어 또는 제목을 검색하세요" />
    </StyledSearchBoxDiv>
  );
};

const StyledSearchBoxDiv = styled.div`
  margin: 0;
  padding: 10px;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid lightgray;
`;

const StyledSelectOption = styled.select`
  margin: 0;
  padding: 0;
  border: none;
`;

const StyledSearchInput = styled.input`
  width: 100%;
  border: none;
  outline-style: none;
  font-size: 12pt;
`;

export default SearchBoxInSearchCourse;
