import React from "react";
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";
import styled from "styled-components";
import Login from "./pages/Login";
import Home from "./pages/Home";
import MapStore from "./store/map";
import MarkerStore from "./store/marker";

function App() {
  const user_login = true;
  if (user_login) {
    return (
      <AppDiv>
        <MapStore>
          <MarkerStore>
            <Home />
          </MarkerStore>
        </MapStore>
      </AppDiv>
    );
  } else {
    return (
      <AppDiv>
        <Login />
      </AppDiv>
    );
  }
}

const AppDiv = styled.div`
  text-align: center;
  height: 100%;
`;

export default App;
