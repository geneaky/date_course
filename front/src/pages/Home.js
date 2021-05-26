/*global kakao*/
import React, { useEffect } from "react";
import styled from "styled-components";
import Header from "../components/Header";
import CourseMenu from "../components/CourseMenu";
import { useDispatch, useSelector } from "react-redux";
import { getUserInfo, setMap } from "../store/store";
import axios from "axios";
import UserMenu from "../components/user/UserMenu";

const Home = () => {
  const token = localStorage.getItem("accessToken");
  const sideMenu = useSelector((store) => store.sideMenu);
  const userMenu = useSelector((store) => store.userMenu);
  const dispatcher = useDispatch();

  useEffect(async () => {
    mapScript();
    if (token) {
      const res = await axios.get("/user/info", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      dispatcher(getUserInfo(res.data));
    }
  }, [token]);

  const mapScript = () => {
    const container = document.getElementById("map");
    const options = {
      center: new kakao.maps.LatLng(37.564213, 127.001698),
      level: 3,
    };
    const defaultMap = new kakao.maps.Map(container, options);

    dispatcher(setMap(defaultMap));
  };

  return (
    <div>
      <Header />
      <HomeBody>
        {sideMenu ? <CourseMenu /> : null}
        {userMenu ? <UserMenu /> : null}
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
