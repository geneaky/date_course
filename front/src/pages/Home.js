/*global kakao*/
import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Header from "../components/Header";
import CourseMenu from "../components/CourseMenu";
import { useDispatch, useSelector } from "react-redux";
import { getToken, getUserInfo, setMap } from "../store/store";
import axios from "axios";

const Home = () => {
  const [sideMenu, setSideMenu] = useState(false);
  const user = useSelector((store) => store.user);
  const token = useSelector((store) => store.token);
  const dispatcher = useDispatch();

  useEffect(() => {
    mapScript();
  }, []);

  const mapScript = () => {
    const container = document.getElementById("map");
    const options = {
      center: new kakao.maps.LatLng(37.564213, 127.001698),
      level: 5,
    };
    const defaultMap = new kakao.maps.Map(container, options);

    dispatcher(setMap(defaultMap));
  };

  return (
    <div>
      <Header sideMenu={sideMenu} setSideMenu={setSideMenu} />
      <HomeBody>
        {sideMenu ? <CourseMenu /> : null}
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
