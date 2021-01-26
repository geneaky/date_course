import React,{useState} from 'react';
import './CourseMenu.css';
import SearchCourse from './SearchCourse';
import MakeCourse from './MakeCourse';

const CourseMenu = () =>{
    const [toggleButton,setToggleButton] = useState('코스 만들기');

    const presentButton = () =>{
        if(toggleButton==='코스 만들기'){
            setToggleButton('코스 찾기');
        }else{
            setToggleButton('코스 만들기');
        }
    }
    return(
        <div className="CourseMenu">
            <button onClick={presentButton}>{toggleButton}</button>
            {toggleButton==='코스 만들기'? <SearchCourse/>:<MakeCourse/>}
        </div>
    )
}

export default CourseMenu