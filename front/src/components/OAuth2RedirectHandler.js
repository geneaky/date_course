import React from "react";
import { Redirect, useLocation } from "react-router-dom";

const OAuth2RedirectHander = () => {
  // const location = useLocation();
  const location = "http://3.34.83.151:80";

  const getUrlParameter = (name) => {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    const regex = new RegExp("[\\?&]" + name + "=([^&#]*)");

    const result = regex.exec(location.search);
    return result === null
      ? ""
      : decodeURIComponent(result[1].replace(/\+/g, " "));
  };

  const token = getUrlParameter("token");
  const error = getUrlParameter("error");

  if (token) {
    localStorage.setItem("accessToken", token);
    return (
      <Redirect
        to={{
          pathname: "/",
          state: { from: location },
        }}
      />
    );
  } else {
    return (
      <Redirect
        to={{
          pathname: "/login",
          state: {
            from: location,
            error: error,
          },
        }}
      />
    );
  }
};

export default OAuth2RedirectHander;
