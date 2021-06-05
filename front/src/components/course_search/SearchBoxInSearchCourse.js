import React, { useState } from "react";
import styled from "styled-components";
import SearchIcon from "@material-ui/icons/Search";

const SearchBoxInSearchCourse = () => {
  const [searchKeyWord, setSearchKeyWord] = useState();

  const searchCourseList = (e) => {
    if (e.key === "Enter") {
      setSearchKeyWord("");
    }
  };
  return (
    <StyledSearchBoxDiv>
      <SearchIcon />
      <StyledSelectOption>
        <option value={"recent"}>최신순</option>
        <option value={"thumbUp"}>인기순</option>
        <option value={"currentLocation"}>거리순</option>
      </StyledSelectOption>
      <StyledSearchInput
        placeholder="#검색어 또는 제목을 검색하세요"
        value={searchKeyWord}
        onKeyPress={searchCourseList}
        onChange={(e) => setSearchKeyWord(e.target.value)}
      />
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
