/* eslint-disable jsx-a11y/anchor-is-valid */

import { useContext, useEffect, useState } from "react";

import AuthContext from "../../context/auth/authContext";

const Login = () => {
  const authContext = useContext(AuthContext);
  const { isLoggedIn, loginUser } = authContext;
  const [userLoginDto, setUserLoginDto] = useState({
    loginId: "",
    password: "",
  });

  const onChange = (e) => {
    const { name, value } = e.target;
    setUserLoginDto((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  useEffect(() => {
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);


  const onSubmit = async (e) => {
    e.preventDefault();
    loginUser(userLoginDto);

    if (isLoggedIn) {
      setUserLoginDto({
        loginId: "",
        password: "",
      });
    }
  };
  return (
    <div className="h-screen">
      <div className="py-36">
        <div className="flex bg-white rounded-lg shadow-lg overflow-hidden mx-auto max-w-sm lg:max-w-4xl">
          <div
            className="hidden lg:block lg:w-1/2 bg-cover"
            style={{
              backgroundImage: `url(https://epe.brightspotcdn.com/81/86/fb9958a549d28c0d58b434316e44/book-purchasing-021323-479704265.jpg)`,
            }}
          ></div>
          <div className="w-full p-8 lg:w-1/2">
            <h2 className="text-2xl font-semibold text-gray-700 text-center">
              Beetech
            </h2>
            <p className="text-xl text-gray-600 text-center">Welcome back!</p>
            <br />
     
            <div className="mt-4 flex items-center justify-between">
              <span className="border-b w-1/5 lg:w-1/4"></span>
              <a
                href="#"
                className="text-xs text-center text-gray-500 uppercase"
              >
                login with email
              </a>
              <span className="border-b w-1/5 lg:w-1/4"></span>
            </div>
            <div className="mt-4">
              <label className="block text-gray-700 text-sm font-bold mb-2">
                Email Address
              </label>
              <input
                className="bg-gray-200 text-gray-700 focus:outline-none focus:shadow-outline border border-gray-300 rounded py-2 px-4 block w-full appearance-none"
                type="loginId"
                name="loginId"
                value={userLoginDto.loginId}
                onChange={onChange}
              />
            </div>
            <div className="mt-4">
              <div className="flex justify-between">
                <label className="block text-gray-700 text-sm font-bold mb-2">
                  Password
                </label>
                <a href="/RequestPassword" className="text-xs text-gray-500">
                  Forget Password?
                </a>
              </div>
              <input
                className="bg-gray-200 text-gray-700 focus:outline-none focus:shadow-outline border border-gray-300 rounded py-2 px-4 block w-full appearance-none"
                type="password"
                name="password"
                value={userLoginDto.password}
                onChange={onChange}
              />
            </div>
            <div className="mt-8">
              <button
                onClick={onSubmit}
                className="bg-gray-700 text-white font-bold py-2 px-4 w-full rounded hover:bg-gray-600"
              >
                Login
              </button>
            </div>
            <div className="mt-4 flex items-center justify-between">
              <span className="border-b w-1/5 md:w-1/4"></span>
              <a href="/register" className="text-xs text-gray-500 uppercase">
                or sign up
              </a>
              <span className="border-b w-1/5 md:w-1/4"></span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
