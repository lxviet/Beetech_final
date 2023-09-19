import {
  USER_LOGIN,
  USER_LOGOUT,
} from "./types";

// eslint-disable-next-line import/no-anonymous-default-export
export default (state, action) => {
  switch (action.type) {
    case USER_LOGIN:
      return {
        ...state,
        isLoggedIn: true,
        userToken: action.payload.data,
        shareLinkItems: [
          {
            id: 1,
            title: "Beetech Shop",
            url: "/",
          },
        ],
        authLinkItems: [
          {
            id: 1,
            title: "Profile",
            url: "/profile",
          },
          {
            id: 2,
            title: "Cart",
            url: "/cart",
          },
        ],
      };
   
    case USER_LOGOUT:
      return {
        ...state,
        isLoggedIn: false,
        TablesData: [],
        role: [],
        userToken: "",
        shareLinkItems: [
          {
            id: 1,
            title: "Beetech Shop",
            url: "/",
          },
        ],
        authLinkItems: [
          {
            id: 1,
            title: "Login",
            url: "/login",
          },
          {
            id: 2,
            title: "Cart",
            url: "/cart",
          },
        ],
      };
   
    default:
      return state;
  }
};
