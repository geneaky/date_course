/*global kakao*/
import React,{createContext, useReducer} from 'react';
export const MapContext = createContext(null);

const reducer = (state,action) => {
    switch(action.type){
        case 'SET_MAP':
            return action.map
    }
}

const MapStore = (props) => {
    const [state,dispatch] = useReducer(reducer,null);
    const map ={
        state:state,
        dispatch:dispatch
    }
    return(
        <MapContext.Provider value={map}>{props.children}</MapContext.Provider>
    )
}

export default MapStore