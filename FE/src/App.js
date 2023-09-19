
import React from "react";
import { BrowserRouter as Router } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./App.css";
import AuthState from "./context/auth/AuthState";
import Footer from "./layout/Footer";
import Home from "./layout/Home";
import Navbar from "./layout/Navbar";

function App() {
  return (
    <>
          <AuthState>
                <Router>
                  <div className="App">
                    <div>
                      <Navbar />
                    </div>
                    <div className=" dark:bg-gray-900 ">
                      <ToastContainer />
                      <Home />
                      <div className="dark:bg-gray-900">
                        <br />
                        <br />
                        <br /> <br /> <br />
                      </div>
                    </div>
                    <div className="footer">
                      <Footer />
                    </div>
                  </div>
                </Router>
          </AuthState>
     
    </>
  );
}

export default App;
