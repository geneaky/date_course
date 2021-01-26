/*global kakao*/
import React,{useEffect,useState} from 'react';
import './Home.css';
import Header from './Header';
import CourseMenu from './CourseMenu';

const Home = () => {
  const [sideMenu,setSideMenu] = useState(false);

  useEffect(()=>{mapScript()},[]);

  const mapScript = () => {

      const container = document.getElementById('map');
      const options = { 
            center: new kakao.maps.LatLng(37.564213, 127.001698), 
            level: 5 
          };
      const map = new kakao.maps.Map(container, options);

      kakao.maps.event.addListener(map,'click',function(mouseEvent){
        let marker = new kakao.maps.Marker({
          map:map,
          position: new kakao.maps.LatLng(mouseEvent.latLng.Ma,mouseEvent.latLng.La)
        })
        marker.setMap(map)
      });

      const places = new kakao.maps.services.Places();

      let callback = function(data,status,pagination){
        if(status === kakao.maps.services.Status.OK){
          console.log(data);
        }
      }
      places.keywordSearch('목동',callback);
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
