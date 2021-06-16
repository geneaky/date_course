import React from "react";
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";
import styled from "styled-components";
import Login from "./pages/Login";
import Home from "./pages/Home";
import OAuth2RedirectHander from "./components/OAuth2RedirectHandler";
import MyCourse from "./pages/MyCourse";
import SavedCourse from "./pages/SavedCourse";

function App() {
  return (
    <AppDiv>
      <Route path="/" exact={true} component={Home} />
      <Route path="/login" exact={true} component={Login} />
      <Route path="/myCourse" exact={true} component={MyCourse} />
      <Route path="/savedCourse" exact={true} component={SavedCourse} />
      <Route
        path="/oauth2/redirect"
        exact={true}
        component={OAuth2RedirectHander}
      />
    </AppDiv>
  );
}

const AppDiv = styled.div`
  text-align: center;
  height: 100%;
`;

export default App;
