import React,{createContext,useReducer} from 'react';
export const SearchContext = createContext(null);

const reducer = (state,action) => {
    switch(action.type){
        case 'SET_PLACES':
            return action.places
    }
};

const SearchStore = (props) => {
    const [state,dispatch] = useReducer(reducer,[]);
    const places = {
        state:state,
        dispatch:dispatch
    }
    return(
        <SearchContext.Provider value={places}>{props.children}</SearchContext.Provider>
    )
}

export default SearchStore