import React from 'react';
import SearchBox from './SearchBox';
import SearchResultBox from './SearchResultBox';
import SearchStore from './store/search';
import RegisterCourse from './RegisterCourse';
import CourseStore from './store/course';

const MakeCourse = () => {
    
    return(
        <div>
            <CourseStore>
                <SearchStore>
                    <SearchBox/>
                    <SearchResultBox/>
                </SearchStore>
                <RegisterCourse/>
            </CourseStore>
        </div>
    )
}

export default MakeCourse