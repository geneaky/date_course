/*global kakao*/
import React,{useEffect,useState} from 'react';
import './Home.css';
import WholeCourse from './WholeCourse'
import CourseMenu from './CourseMenu'

const Home = () => {
  useEffect(async()=>{
      await kakao.maps.load(()=>{
        let container = document.getElementById('map');
        let options = { 
	            center: new kakao.maps.LatLng(37.564213, 127.001698), 
	            level: 5 
            };
        const map = new kakao.maps.Map(container, options);
      })
  },[]);

  sdfsf

  return (
    <div className="Home">
      <CourseMenu/>
      <div id="map"></div>
      <WholeCourse/>
    </div>
  );
};

export default Home;
