import React,{useContext, useEffect} from 'react';
import PlaceInfo from './PlaceInfo';
import {SearchContext} from './store/search';
import './SearchResultBox.css'


const SearchResultBox = () => {
    const context = useContext(SearchContext);

    return(
        <div className="SearchResultBox">
            {context.state.map((place,index)=><PlaceInfo key={index} result={place}/>)}
        </div>
    )
}

export default SearchResultBox