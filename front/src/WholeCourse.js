import React from 'react'
import CourseList from './CourseList'
import CourseSearch from './CourseSearch'

const WholeCourse = () => {

    return(
        <div>
            코스 검색과 리스트를 보여줍니다.
            <CourseSearch/>
            <CourseList/>
        </div>
    )
}

export default WholeCourse;