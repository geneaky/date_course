/*global kakao*/
import React, { useEffect, useState, useContext } from "react";
import styled from "styled-components";
import Header from "../components/Header";
import CourseMenu from "../components/CourseMenu";
import { MapContext } from "../store/map";

const Home = () => {
  const [sideMenu, setSideMenu] = useState(false);
  const map = useContext(MapContext);

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

    map.dispatch({ type: "SET_MAP", map: defaultMap });
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
