import React from "react";
import { useSelector } from "react-redux";
import styled from "styled-components";

const Comment = ({ comment }) => {
  const user = useSelector((store) => store.user);
  return (
    <div>
      {comment.userName === user.name ? (
        <StyledSelfCommentDiv>
          <p>{comment.commentContent}</p>
        </StyledSelfCommentDiv>
      ) : (
        <StyledCommentDiv>
          <p>
            <span>{comment.userName}:</span>
            <span>{comment.commentContent}</span>
          </p>
        </StyledCommentDiv>
      )}
    </div>
  );
};

const StyledCommentDiv = styled.div`
  width: 100%;
  p {
    width: 40%;
    border: 1px solid black;
    border-radius: 5px;
    padding-left: 10px;
    text-align: left;
  }
`;
const StyledSelfCommentDiv = styled.div`
  width: 100%;
  p {
    width: 40%;
    border: 1px solid black;
    border-radius: 5px;
    padding-right: 10px;
    margin-left: 55%;
    text-align: right;
  }
`;

export default Comment;
