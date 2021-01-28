import React,{createContext,useReducer} from 'react';
export const MarkerContext = createContext(null);
const reducer= (state,action) => {
    switch(action.type){
        case 'SET_MARKER':
            return state.concat([action.marker])
        case 'CLEAR_MARKERS':
            for(let i of state){
                i.setMap(null);
            }
            return []
    }
}

const MarkerStore = (props) => {
    const [state,dispatch] = useReducer(reducer,[]);

    const marker = {
        state:state,
        dispatch:dispatch
    }
    return(
        <MarkerContext.Provider value={marker}>{props.children}</MarkerContext.Provider>
    )
}

export default MarkerStore