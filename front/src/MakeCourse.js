import React from 'react';
import SearchBox from './SearchBox';
import SearchResultBox from './SearchResultBox';
import SearchStore from './store/search';

const MakeCourse = () => {
    
    return(
        <div>
            <SearchStore>
             <SearchBox/>
             <SearchResultBox/>
            </SearchStore>
        </div>
    )
}

export default MakeCourse