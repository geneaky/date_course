import React from "react";
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";
import styled from "styled-components";
import Login from "./pages/Login";
import Home from "./pages/Home";

function App() {
  return (
    <AppDiv>
      <Route path="/" exact={true} component={Home} />
      <Route path="/login" exact={true} component={Login} />
    </AppDiv>
  );
}

const AppDiv = styled.div`
  text-align: center;
  height: 100%;
`;

export default App;
