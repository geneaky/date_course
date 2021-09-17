/*global kakao*/
import React, { useEffect } from "react";
import styled from "styled-components";
import Header from "../components/Header";
import CourseMenu from "../components/CourseMenu";
import MyCourse from "../components/user/MyCourse";
import SavedCourse from "../components/user/SavedCourse";
import { useDispatch, useSelector } from "react-redux";
import { getUserInfo, setMap } from "../store/store";
import axios from "axios";
import UserMenu from "../components/user/UserMenu";

const Home = () => {
  const sideMenu = useSelector((store) => store.sideMenu);
  const userMenu = useSelector((store) => store.userMenu);
  const myCourseMenu = useSelector((store) => store.myCourseMenu);
  const savedCourseMenu = useSelector((store) => store.savedCourseMenu);
  const dispatcher = useDispatch();

  useEffect(async () => {
    mapScript();
    const res = await axios.get("/user/info");
    dispatcher(getUserInfo(res.data));
  });

  const mapScript = () => {
    const container = document.getElementById("map");
    const options = {
      center: new kakao.maps.LatLng(37.564213, 127.001698),
      level: 3,
    };
    const defaultMap = new kakao.maps.Map(container, options);
    defaultMap.relayout();
    dispatcher(setMap(defaultMap));
  };

  return (
    <div>
      <Header />
      <HomeBody>
        {sideMenu ? <CourseMenu /> : null}
        {userMenu ? <UserMenu /> : null}
        {myCourseMenu ? <MyCourse /> : null}
        {savedCourseMenu ? <SavedCourse /> : null}
        <Map id="map"></Map>
      </HomeBody>
    </div>
  );
};

const HomeBody = styled.div`
  display: flex;
`;

const Map = styled.div`
  width: 100%;
  height: 800px;
`;

export default Home;
