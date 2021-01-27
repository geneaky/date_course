/*global kakao*/
import React,{useEffect,useState,useContext} from 'react';
import SearchIcon from '@material-ui/icons/Search';
import './SearchBox.css';
import {SearchContext} from './store/search';

const SearchBox = () => {
    const [searchResult,setSearchResult] = useState([]);
    const context = useContext(SearchContext);

    useEffect(()=>{
        const places = new kakao.maps.services.Places();
        let callback = function(data,status){
            if(status===kakao.maps.services.Status.OK){
                let dataList = []
                for(let i in data){
                    dataList.push([data[i].place_name,data[i].address_name,data[i].phone])
                }
                context.dispatch({type:'SET_PLACES',places:dataList})
            }
        }
        places.keywordSearch(searchResult,callback);
    },[searchResult]);

    const onKeyPress = (e) => {
        if(e.key === 'Enter'){
            setSearchResult(e.target.value);
        }
    }

    return(
        <div className="SearchBox">
            <SearchIcon/>
            <input placeholder="장소 또는 지역 이름을 검색하세요" onKeyPress={onKeyPress}/>
        </div>
    )
}

export default SearchBox

