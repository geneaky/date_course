/*global kakao*/
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import styled from "styled-components";
import { setMarker, setSelectedDatecourse } from "../../store/store";
import FavoriteBorderIcon from "@material-ui/icons/FavoriteBorder";
import FavoriteIcon from "@material-ui/icons/Favorite";
import ChatBubbleOutlineIcon from "@material-ui/icons/ChatBubbleOutline";

const CourseInfo = ({ result }) => {
  const map = useSelector((store) => store.map);
  const selectedMarker = useSelector((store) => store.marker);
  const userLikedCourse = useSelector((store) => store.userLikedCourse);
  const dispatcher = useDispatch();

  useEffect(() => {
    selectedMarker.forEach((marker) => {
      marker.setMap(null);
    });
    dispatcher(setSelectedDatecourse(null));
  }, []);

  const initiateDatecourse = () => {
    dispatcher(setSelectedDatecourse(result));
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
  };
  return (
    <StyledCourseInfoDiv onClick={initiateDatecourse}>
      <p>{result.dateCourseTitle}</p>
      <span>{result.userName}</span>
      <span>
        {userLikedCourse?.includes(result.id) ? (
          <FavoriteIcon style={{ fontSize: 20 }} />
        ) : (
          <FavoriteBorderIcon style={{ fontSize: 20 }} />
        )}
        {result.thumbUp}
      </span>
      <span>
        <ChatBubbleOutlineIcon style={{ fontSize: 20 }} />
        {result.comments.length}
      </span>
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

  span {
    margin: 10px;
  }
`;

export default CourseInfo;
