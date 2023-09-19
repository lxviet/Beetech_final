import React, { useContext } from "react";
import { Route, Switch } from "react-router-dom";
import AuthContext from "../context/auth/authContext";
import Login from "../pages/Unauthenticated/Login";
import Main from "../pages/Unauthenticated/Main";
import NotFound from "../pages/error/NotFound";
import Cart from "../pages/user/Cart";


const Home = () => {
  const authContext = useContext(AuthContext);
  const { isLoggedIn, role } = authContext;
  //const isUser = role.includes("User");

  return (
    <>
      <Switch>
        <Route exact path="/">
          <Main />
        </Route>

        <Route exact path="/login">
          {isLoggedIn ?   <Main />  : <Login />}
        </Route>
        <Route exact path="/cart">
          {isLoggedIn ? <Cart /> : <Login />}
        </Route>
        <Route path="" component={NotFound} />
      </Switch>
    </>
  );
};

export default Home;
