/*global kakao*/
import React,{useEffect,useState,useContext} from 'react';
import './Home.css';
import Header from './Header';
import CourseMenu from './CourseMenu';
import {MapContext} from './store/map';

const Home = () => {
  const [sideMenu,setSideMenu] = useState(false);
  const map = useContext(MapContext);

  useEffect(()=>{mapScript()},[]);

  const mapScript = () => {
      const container = document.getElementById('map');
      const options = { 
            center: new kakao.maps.LatLng(37.564213, 127.001698), 
            level: 5 
          };
      const defaultMap = new kakao.maps.Map(container, options);

      map.dispatch({type:'SET_MAP',map:defaultMap})
  }

  return (
    <div className="Home">
      <Header sideMenu={sideMenu} setSideMenu={setSideMenu}/>
      <div className="HomeBody">
      {sideMenu ? <CourseMenu/> : null}
      <div id="map"></div>
      </div>
    </div>
  );
};

export default Home;
