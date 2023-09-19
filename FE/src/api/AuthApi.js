import axios from "axios";

export default class AuthApi {
  constructor() {
    this.client = null;
    this.api_url = "http://localhost:8080/api/v1";
  }

  init = () => {
    this.client = new axios.create({
      baseURL: this.api_url,
    });
    return this.client;
  };
  register = (user) => {
    return this.init().post("/auth/register", user, { withCredentials: true });
  };

  login = (userLoginDto) => {
    return this.init().post("/auth/login", userLoginDto, { withCredentials: true });
  };

  logout = () => {
    return this.init().post("/auth/logout", null, { withCredentials: true });
  };

}
