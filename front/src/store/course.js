import React,{createContext,useReducer} from 'react';
export const CourseContext = createContext(null);

const reducer = (state,action) => {
    switch(action.type){
        case 'REGISTER_COURSE':
            return action.course
        case 'RESET_COURSE':
            return []
        
    }
}

const CourseStore = (props) => {
    const [state,dispatch] = useReducer(reducer,[]);
    const course = {
        state:state,
        dispatch:dispatch
    }
    return(
        <CourseContext.Provider value={course}>{props.children}</CourseContext.Provider>
    )
}

export default CourseStore