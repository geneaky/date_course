import React,{useContext} from 'react';
import {CourseContext} from './store/course';

const RegisterCourse = () => {
    const courseContext = useContext(CourseContext);
    return(
        <div>
                <p>{courseContext.state[0]}</p>
                <input type="file" multiple/>
                {/* 다중 이미지 미리보기 적용하고 사이트에서 광고창 넘기듯이 적용 */}
                <br/>
                <textarea value={"글 작성"}/>
                <br/>
                <textarea value={"태그 등록"}/>
                <br/>
                <span>
                    <button>이전코스</button>
                    <button>등록</button>
                    <button>다음코스</button>
                </span>
        </div>
    )
}

export default RegisterCourse