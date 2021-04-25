/*global kakao*/
import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Header from "../components/Header";
import CourseMenu from "../components/CourseMenu";
import { useDispatch, useSelector } from "react-redux";
import { getUserInfo, setMap } from "../store/store";
import axios from "axios";

const Home = () => {
  const [sideMenu, setSideMenu] = useState(false);
  const user = useSelector((store) => store.user);
  const dispatcher = useDispatch();

  useEffect(() => {
    mapScript();
  }, []);

  useEffect(() => {
    axios
      .get("/user/info")
      .then((res) => dispatcher(getUserInfo(res.data)))
      .catch((Error) => {
        console.log(Error);
      });
    console.log(user);
  }, [user]);

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
