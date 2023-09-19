import React, { useEffect, useReducer } from "react";
import "react-toastify/dist/ReactToastify.css";
import AuthApi from "../../api/AuthApi";
import AuthenContext from "./authContext";
import AuthenReducer from "./authReducer";

import { toast } from "react-toastify";
import {
  USER_LOGIN,
  USER_LOGOUT,
} from "./types";

const AuthState = (props) => {
  const initialState = {
    isLoggedIn: false,
    role: [],
    userToken: "",
    shareLinkItems: [
      {
        id: 1,
        title: "Beetech",
        url: "/",
      }
    ],
    authLinkItems: [
      {
        id: 1,
        title: "Login",
        url: "/login",
      },
    ],
  };
  const [state, dispatch] = useReducer(AuthenReducer, initialState, () => {
    const localState = localStorage.getItem("localState");
    return localState ? JSON.parse(localState) : initialState;
  });
  useEffect(() => {
    localStorage.setItem("localState", JSON.stringify(state));
  }, [state]);

  const authApi = new AuthApi();

  const loginUser = async (userLoginDto) => {
    console.log("test");
    const response = await authApi.login(userLoginDto);
    
    console.log(response);

    if (response.status === 200) {
      dispatch({
        type: USER_LOGIN,
        payload: response.data,
      });
    }
    toast.success("Logged in");
  };

  const logoutUser = async () => {
    dispatch({
      type: USER_LOGOUT,
    });
    toast.success("Logged out");

  };
  return (
    <AuthenContext.Provider
      value={{
        isLoggedIn: state.isLoggedIn,
        userData: state.userData,
        role: state.role,
        shareLinkItems: state.shareLinkItems,
        authLinkItems: state.authLinkItems,
        userToken: state.userToken,
        loginUser,
        logoutUser,
      }}
    >
      {props.children}
    </AuthenContext.Provider>
  );
};

export default AuthState;
