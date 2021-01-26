import React from 'react'
import {BrowserRouter as Router,
        Switch,
        Route,
        Link
} from 'react-router-dom';
import './App.css';
import Login from './Login';
import Home from './Home';

function App() {
  const user_login = true
  if(user_login){
    return(
      <div className="App">
        <Home/>
      </div>
    )
  }
  else{
    return(
      <div className="App">
        <Login/>
      </div>
    )
  }
}

export default App;
