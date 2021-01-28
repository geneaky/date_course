/*global kakao*/
import React, { useEffect,useContext} from 'react';
import './PlaceInfo.css';
import {MapContext} from './store/map';
import {MarkerContext} from './store/marker';

const PlaceInfo = ({result}) => {
    const map = useContext(MapContext);
    const markerContext = useContext(MarkerContext);
    useEffect(()=>{
        let marker = new kakao.maps.Marker({
            map:map.state,
            position: new kakao.maps.LatLng(result[4],result[3])
        })
        marker.setMap(map.state);
        markerContext.dispatch({type:'SET_MARKER',marker:marker})
    },[])

    return(
        <div className="PlaceInfo">
            <p>{result[0]}</p>
            <p>{result[1]}</p>
            <p>{result[2]}</p>
        </div>
    )
}

export default PlaceInfo