/*global kakao*/
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import styled from "styled-components";
import FavoriteBorderIcon from "@material-ui/icons/FavoriteBorder";
import FavoriteIcon from "@material-ui/icons/Favorite";
import axios from "axios";
import { likedCourseList } from "../../api/UserApi";
import { searchRecentDateCourseList } from "../../api/DateCourseApi";

const S3Url = "https://datecourse.s3.ap-northeast-2.amazonaws.com/";

const CourseDetail = ({ course }) => {
  const [recheck, setRecheck] = useState(false);
  const [courseLength, setCourseLength] = useState(0);
  const [comment, setComment] = useState("");
  const map = useSelector((store) => store.map);
  const userLikedCourse = useSelector((store) => store.userLikedCourse);
  const user = useSelector((store) => store.user);
  const dispatcher = useDispatch();

  const selectedCourseLength = course.locations?.length - 1;
  const Url = course.locations[courseLength]?.photoUrl;
  let imageUrl = null;
  if (Url !== "") {
    imageUrl = S3Url + Url;
  }

  useEffect(() => {
    searchRecentDateCourseList(dispatcher);
    likedCourseList(dispatcher);
  }, [recheck]);

  useEffect(() => {
    map.setCenter(
      new kakao.maps.LatLng(
        course.locations[courseLength]?.posy,
        course.locations[courseLength]?.posx
      )
    );
  });

  const saveCourse = () => {
    axios.post("/user/likecourse", null);
  };

  const thumbUp = async () => {
    const token = localStorage.getItem("accessToken");
    const url = `/datecourse/like/${course.id}`;
    const config = {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
    await axios.get(url, config);
    setRecheck(!recheck);
  };

  const commentRegist = () => {
    if (user) {
      const token = localStorage.getItem("accessToken");
      const url = `/datecourse/comment/${course.id}`;
      const config = {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      };
      axios.post(url, comment, config);
    } else {
      alert("댓글을 달고싶다면 로그인해주세요");
    }
    setComment("");
  };

  return (
    <div>
      <StlyedImgDiv>
        {imageUrl ? (
          <img alt="memory" src={imageUrl} />
        ) : (
          <img src={"/no-image.png"} alt="any-pic" />
        )}
      </StlyedImgDiv>
      <StyledPreInfoDiv>
        {userLikedCourse?.includes(course.id) ? (
          <FavoriteIcon style={{ fontSize: 20 }} onClick={thumbUp} />
        ) : (
          <FavoriteBorderIcon style={{ fontSize: 20 }} onClick={thumbUp} />
        )}
        <p>{course.locations[courseLength]?.tags}</p>
      </StyledPreInfoDiv>
      <StyledUserTextDiv>
        <p>{course.userName} </p>
        <p>{course.locations[courseLength]?.text}</p>
      </StyledUserTextDiv>
      <StyledCommentDiv>
        <StyledCommentListDiv>
          comment
          {/* 현재 달려있는 댓글 */}
          {/* 현재 달려있는 댓글에 대댓글 1 depth */}
          {/* 댓글에 좋아요 no depth */}
          {/* course.comment.map((one)=><Commend prop={one}/>) */}
          {/* 댓글 달기 */}
        </StyledCommentListDiv>
        <StyledCommentInputDiv>
          <input type="text" onChange={(e) => setComment(e.target.value)} />
          <button
            onClick={commentRegist}
            onKeyPress={(e) => {
              if (e.key === "Enter") commentRegist();
            }}
          >
            등록
          </button>
        </StyledCommentInputDiv>
      </StyledCommentDiv>
      <StyledCourseButtonDiv>
        <button
          onClick={() => {
            courseLength === 0
              ? setCourseLength(selectedCourseLength)
              : setCourseLength(courseLength - 1);
          }}
        >
          이전 코스
        </button>
        <button onClick={saveCourse}>Save</button>
        <button
          onClick={() => {
            courseLength >= selectedCourseLength
              ? setCourseLength(0)
              : setCourseLength(courseLength + 1);
          }}
        >
          다음 코스
        </button>
      </StyledCourseButtonDiv>
    </div>
  );
};

const StlyedImgDiv = styled.div`
  width: 100%;
  height: 240px;
  border-bottom: 1px solid lightgray;
  padding: 0;
  margin: 0;
  img {
    margin: 0;
    padding: 0;
    max-width: 100%;
    max-height: 100%;
  }
`;

const StyledPreInfoDiv = styled.div`
  display: flex;
  width: 100%;
  margin: 0;
  padding: 0;

  * {
    margin: auto 5px;
    font-size: 10pt;
  }
`;

const StyledUserTextDiv = styled.div`
  display: flex;
  width: 100%;
  height: 30px;
  overflow: auto;
  * {
    font-weight: 550;
    margin: 0 5px;
  }
`;

const StyledCommentDiv = styled.div`
  border-top: 1px solid lightgray;
  border-bottom: 1px solid lightgray;
  width: 100%;
  height: 178px;
`;

const StyledCourseButtonDiv = styled.div`
  width: 100%;
  height: 26px;
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: space-between;
  button {
    width: 33.33%;
    font-family: "Roboto", sans-serif;
    font-size: 11px;
    text-transform: uppercase;
    letter-spacing: 2.5px;
    font-weight: 500;
    color: #000;
    background-color: #ffdab9;
    border: none;
    box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease 0s;
    cursor: pointer;
    outline: none;
    margin: 0 1px;

    :hover {
      background-color: #ffa07a;
      box-shadow: 0px 15px 20px rgba(223, 101, 45, 0.835);
      color: #fff;
      transform: translateY(-7px);
    }
  }
`;

const StyledCommentListDiv = styled.div`
  width: 100%;
  height: 150px;
  overflow: auto;
`;
const StyledCommentInputDiv = styled.div`
  padding: 0;
  margin: 0;
  width: 100%;
  input {
    padding: 0;
    margin: 0;
    width: 90%;
    height: 25px;
    border: 2px solid lightgray;
    outline: none;
  }
  button {
    background-color: #ffa07a;
    border: none;
    outline: none;
    cursor: pointer;
    padding: 0;
    margin: 0;
    width: 9%;
    height: 27px;
    font-family: "Roboto", sans-serif;
    font-size: 11px;
    text-transform: uppercase;
    letter-spacing: 2.5px;
    font-weight: 500;
    color: #000;
  }
`;
export default CourseDetail;
