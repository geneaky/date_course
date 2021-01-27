import React from 'react';
import './PlaceInfo.css';

const PlaceInfo = ({result}) => {

    return(
        <div className="PlaceInfo">
            <p>{result[0]}</p>
            <p>{result[1]}</p>
            <p>{result[2]}</p>
        </div>
    )
}

export default PlaceInfo