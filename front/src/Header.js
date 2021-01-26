import React from 'react';
import './Header.css'
import MenuIcon from '@material-ui/icons/Menu';

const Header = ({sideMenu,setSideMenu}) => {

    const menuPop = () =>{
        setSideMenu(!sideMenu);
    }
    
    return(
        <div className="Header">
            <MenuIcon onClick={menuPop}/>
            <h3>open date course</h3>
            <p>user</p>
        </div>
    )
}

export default Header;