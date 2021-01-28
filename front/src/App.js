import React from 'react'
import {BrowserRouter as Router,
        Switch,
        Route,
        Link
} from 'react-router-dom';
import './App.css';
import Login from './Login';
import Home from './Home';
import MapStore from './store/map';
import MarkerStore from './store/marker';

function App() {
  const user_login = true
  if(user_login){
    return(
      <div className="App">
        <MapStore>
          <MarkerStore>
            <Home/>
          </MarkerStore>
        </MapStore>
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
